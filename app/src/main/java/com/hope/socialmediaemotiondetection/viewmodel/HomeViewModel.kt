package com.hope.socialmediaemotiondetection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.hope.socialmediaemotiondetection.model.post.Post
import com.hope.socialmediaemotiondetection.model.post.comment.PostComments
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.repository.AuthRepository
import com.hope.socialmediaemotiondetection.repository.PostCommentRepository
import com.hope.socialmediaemotiondetection.repository.PostLikesRepository
import com.hope.socialmediaemotiondetection.repository.PostRepository
import com.hope.socialmediaemotiondetection.repository.UserCommentsRepository
import com.hope.socialmediaemotiondetection.repository.UserDailyEmotionRepository
import com.hope.socialmediaemotiondetection.repository.UserFollowsRepository
import com.hope.socialmediaemotiondetection.repository.UserLikedPostRepository
import com.hope.socialmediaemotiondetection.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val userLikedPostRepository: UserLikedPostRepository,
    private val postLikesRepository: PostLikesRepository,
    private val postCommentRepository: PostCommentRepository,
    private val userCommentsRepository: UserCommentsRepository,
    private val authRepository: AuthRepository,
    private val userFollowsRepository: UserFollowsRepository,
    private val userDailyEmotionRepository: UserDailyEmotionRepository
) : ViewModel() {

    private val _postResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val postResult : StateFlow<Resource<Boolean>> get() = _postResult

    private val _getAllPostResult = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val getAllPostResult : StateFlow<Resource<Boolean>> get() = _getAllPostResult

    private val _getAllPost = MutableStateFlow<Resource<Map<String,List<Post>>>>(Resource.Idle())
    val getAllPost : StateFlow<Resource<Map<String,List<Post>>>> get() = _getAllPost

    private val _gelAllPostShorted = MutableStateFlow<Resource<List<Pair<String, Post>>>>(Resource.Idle())
    val gelAllPostShorted: StateFlow<Resource<List<Pair<String, Post>>>> get() = _gelAllPostShorted


    private val _postLikeResults = MutableStateFlow<Map<String, Resource<Boolean>>>(emptyMap())
    val postLikeResults: StateFlow<Map<String, Resource<Boolean>>> get() = _postLikeResults

    private val _postAddLikeResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val postAddLikeResult : StateFlow<Resource<Boolean>> get() = _postAddLikeResult

    private val _postRemoveLikeResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val postRemoveLikeResult : StateFlow<Resource<Boolean>> get() = _postRemoveLikeResult

    private val _likesCountResult = MutableStateFlow<Resource<Int>>(Resource.Idle())
    val likesCountResult: StateFlow<Resource<Int>> get() = _likesCountResult





    private val _postCommentsResult = MutableStateFlow<Resource<PostComments>>(Resource.Idle())
    val postCommentsResult: StateFlow<Resource<PostComments>> get() = _postCommentsResult

    private val _postAddCommentResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val postAddCommentResult: StateFlow<Resource<Boolean>> get() = _postAddCommentResult

    private val _postRemoveCommentResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val postRemoveCommentResult: StateFlow<Resource<Boolean>> get() = _postRemoveCommentResult

    private val _commentCountResult = MutableStateFlow<Resource<Int>>(Resource.Idle())
    val commentCountResult: StateFlow<Resource<Int>> get() = _commentCountResult

    fun resetPostResult() {
        _postResult.value = Resource.Idle()
    }

    fun addPost(emotion: String, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val postResult = postRepository.addPost(emotion, content)

                if (postResult.isSuccess) {
                    val emotionUpdateResult = userDailyEmotionRepository.updateDailyEmotion(currentDate, emotion, 10)

                    _postResult.value = if (emotionUpdateResult.isSuccess) {
                        Resource.Success(true)
                    } else {
                        Resource.Failure("Post oluşturuldu ancak günlük emotion güncellenemedi.")
                    }
                } else {
                    _postResult.value = Resource.Failure("Post oluşturulamadı.")
                }
            } catch (e: Exception) {
                _postResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }

    private fun sortPostsByCreatedAt(posts: Map<String, List<Post>>): List<Pair<String, Post>> {
        _gelAllPostShorted.value = Resource.Loading()
        return posts.flatMap { (username, postList) ->
            postList.map { post -> username to post }
        }
            .sortedByDescending { (_, post) ->
                val date = when (val createdAt = post.createdAt) {
                    is Timestamp -> createdAt.toDate()
                    is String -> SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(createdAt)
                    is Long -> Date(createdAt)
                    else -> null
                }
                date
            }
    }

    private fun fetchPostsForAllUsers(userIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _gelAllPostShorted.value = Resource.Loading()
            try {
                val postsMap = mutableMapOf<String, List<Post>>()
                val postsList = userIds.map { userId ->
                    async {
                        val usernameResult = userRepository.getUsernameByUserId(userId)
                        val postsResult = postRepository.getPostsByUser(userId)

                        if (usernameResult.isSuccess && postsResult.isSuccess) {
                            val username = usernameResult.getOrNull()
                            val posts = postsResult.getOrNull()
                            if (username != null && posts != null) {
                                username to posts
                            } else {
                                null
                            }
                        } else {
                            null
                        }
                    }
                }.awaitAll()

                postsList.filterNotNull().forEach { (username, posts) ->
                    postsMap[username] = posts
                }

                val sortedPosts = sortPostsByCreatedAt(postsMap)

                _gelAllPostShorted.value = Resource.Success(sortedPosts)

            } catch (e: Exception) {
                _gelAllPostShorted.value = Resource.Failure(e.message ?: "An error occurred")
            }
        }
    }
    fun getFollowingAllPost() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _getAllPost.value = Resource.Loading()
                val result = authRepository.currentUser()?.let { user ->
                    userFollowsRepository.getFollowingList(user.uid)
                }
                result?.onSuccess { listOfUserIds ->
                    fetchPostsForAllUsers(listOfUserIds)
                }?.onFailure { exception ->
                    println("Error fetching following list: ${exception.message}")
                }

            } catch (e: Exception) {
                println("Exception: $e")
            }
        }
    }

    fun addLikedPost(postId: String, emotion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _postAddLikeResult.value = Resource.Loading()
                val postLikeResult = postLikesRepository.addLikeToPost(postId)

                if (postLikeResult.isFailure) {
                    _postAddLikeResult.value = Resource.Failure("Beğeni eklenemedi")
                } else {
                    val userLikeResult = userLikedPostRepository.addLikePost(postId)

                    if (userLikeResult.isFailure) {
                        postLikesRepository.removeLikeFromPost(postId)
                        _postAddLikeResult.value = Resource.Failure("Beğeni eklenemedi")
                    } else {
                        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val emotionUpdateResult = userDailyEmotionRepository.updateDailyEmotion(currentDate, emotion, 3)
                        _postAddLikeResult.value = if (emotionUpdateResult.isSuccess) {
                            Resource.Success(true)
                        } else {
                            Resource.Failure("Beğeni eklendi fakat emotion güncellenemedi")
                        }

                        fetchLikesCount(postId)
                        checkPostLikeStatus(postId)
                    }
                }
            } catch (e: Exception) {
                _postAddLikeResult.value = Resource.Failure(e.message ?: "Bir hata oluştu")
            }
        }
    }


    fun addComment(postId: String, content: String, emotion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _postAddCommentResult.value = Resource.Loading()
                val postCommentResult = postCommentRepository.addCommentToPost(postId, content, emotion)
                if (postCommentResult.isFailure) {
                    _postAddCommentResult.value = Resource.Failure("Posta yorum eklenemedi")
                } else {
                    val commentId = postCommentResult.getOrThrow()

                    val userCommentResult = userCommentsRepository.addCommentToUser(postId, content, emotion)

                    if (userCommentResult.isFailure) {
                        postCommentRepository.removeCommentFromPost(postId, commentId)
                        _postAddCommentResult.value = Resource.Failure("Kullanıcıya yorum eklenemedi")
                    } else {
                        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val emotionUpdateResult = userDailyEmotionRepository.updateDailyEmotion(currentDate, emotion, 6)

                        _postAddCommentResult.value = if (emotionUpdateResult.isSuccess) {
                            Resource.Success(true)
                        } else {
                            Resource.Failure("Yorum eklendi fakat emotion güncellenemedi")
                        }
                    }
                }
            } catch (e: Exception) {
                _postAddCommentResult.value = Resource.Failure(e.message ?: "Bir hata oluştu")
            }
        }
    }

    fun removeLikedPost(postId: String, emotion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _postRemoveLikeResult.value = Resource.Loading()
                val postUnlikeResult = postLikesRepository.removeLikeFromPost(postId)

                if (postUnlikeResult.isFailure) {
                    _postRemoveLikeResult.value = Resource.Failure("Beğeni kaldırılamadı")
                } else {
                    val userUnlikeResult = userLikedPostRepository.removeLikedPost(postId)

                    if (userUnlikeResult.isFailure) {
                        postLikesRepository.addLikeToPost(postId)
                        _postRemoveLikeResult.value = Resource.Failure("Beğeni kaldırılamadı")
                    } else {
                        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val emotionUpdateResult = userDailyEmotionRepository.updateDailyEmotion(currentDate, emotion, -3)

                        _postRemoveLikeResult.value = if (emotionUpdateResult.isSuccess) {
                            Resource.Success(true)
                        } else {
                            Resource.Failure("Beğeni kaldırıldı fakat emotion güncellenemedi")
                        }
                        fetchLikesCount(postId)
                        checkPostLikeStatus(postId)
                    }
                }
            } catch (e: Exception) {
                _postRemoveLikeResult.value = Resource.Failure(e.message ?: "Bir hata oluştu")
            }
        }
    }

    fun removeComment(postId: String, commentId: String, emotion: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _postRemoveCommentResult.value = Resource.Loading()
                val removeResult = postCommentRepository.removeCommentFromPost(postId, commentId)
                if (removeResult.isFailure) {
                    _postRemoveCommentResult.value = Resource.Failure("Yorum silinemedi")
                } else {
                    val userRemoveResult = userCommentsRepository.removeCommentFromUser(postId, commentId)

                    if (userRemoveResult.isFailure) {
                        postCommentRepository.addCommentToPost(postId, commentId,emotion)
                        _postRemoveCommentResult.value = Resource.Failure("Kullanıcıdan yorum silinemedi")
                    } else {
                        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val emotionUpdateResult = userDailyEmotionRepository.updateDailyEmotion(currentDate, emotion, -6)

                        _postRemoveCommentResult.value = if (emotionUpdateResult.isSuccess) {
                            Resource.Success(true)
                        } else {
                            Resource.Failure("Yorum silindi fakat emotion güncellenemedi")
                        }
                    }
                }
            } catch (e: Exception) {
                _postRemoveCommentResult.value = Resource.Failure(e.message ?: "Bir hata oluştu")
            }
        }
    }

    private fun fetchLikesCount(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _likesCountResult.value = Resource.Loading()
                val likesCountResult = postLikesRepository.getLikesCount(postId)
                if (likesCountResult.isSuccess) {
                    _likesCountResult.value = Resource.Success(likesCountResult.getOrDefault(0))
                } else {
                    _likesCountResult.value = Resource.Failure("Beğeni sayısı alınamadı")
                }
            } catch (e: Exception) {
                _likesCountResult.value = Resource.Failure(e.message ?: "Bir hata oluştu")
            }
        }
    }

    fun fetchCommentCount(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _commentCountResult.value = Resource.Loading()
                val commentCountResult = postCommentRepository.getCommentsCount(postId)
                if (commentCountResult.isSuccess) {
                    _commentCountResult.value = Resource.Success(commentCountResult.getOrDefault(0))
                } else {
                    _commentCountResult.value = Resource.Failure("Beğeni sayısı alınamadı")
                }
            } catch (e: Exception) {
                _commentCountResult.value = Resource.Failure(e.message ?: "Bir hata oluştu")
            }
        }
    }

    fun checkPostLikeStatus(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val isLikedResult = postLikesRepository.isPostLikedByUser(postId)
                val newPostLikeResults = _postLikeResults.value.toMutableMap()

                if (isLikedResult.isSuccess) {
                    val isLiked = isLikedResult.getOrNull() ?: false
                    newPostLikeResults[postId] = if (isLiked) {
                        Resource.Success(true)
                    } else {
                        Resource.Success(false)
                    }
                } else {
                    newPostLikeResults[postId] = Resource.Failure("Beğeni durumu alınamadı")
                }

                _postLikeResults.value = newPostLikeResults
            } catch (e: Exception) {
                val newPostLikeResults = _postLikeResults.value.toMutableMap()
                newPostLikeResults[postId] = Resource.Failure(e.message ?: "Bir hata oluştu")
                _postLikeResults.value = newPostLikeResults
            }
        }
    }

    fun fetchCommentsForPost(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _postCommentsResult.value = Resource.Loading()
                val commentsResult = postCommentRepository.getCommentsForPost(postId)
                if (commentsResult.isSuccess) {
                    val postComments = commentsResult.getOrNull()
                    if (postComments != null) {
                        _postCommentsResult.value = Resource.Success(postComments)
                    } else {
                        _postCommentsResult.value = Resource.Failure("Yorumlar alınamadı")
                    }
                } else {
                    _postCommentsResult.value = Resource.Failure("Yorumlar alınamadı")
                }
            } catch (e: Exception) {
                _postCommentsResult.value = Resource.Failure(e.message ?: "Bir hata oluştu")
            }
        }
    }

}