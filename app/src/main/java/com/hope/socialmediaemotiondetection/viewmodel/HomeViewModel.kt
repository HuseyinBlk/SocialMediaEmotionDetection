package com.hope.socialmediaemotiondetection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hope.socialmediaemotiondetection.model.post.Post
import com.hope.socialmediaemotiondetection.model.post.comment.PostComments
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.repository.AuthRepository
import com.hope.socialmediaemotiondetection.repository.PostCommentRepository
import com.hope.socialmediaemotiondetection.repository.PostLikesRepository
import com.hope.socialmediaemotiondetection.repository.PostRepository
import com.hope.socialmediaemotiondetection.repository.UserCommentsRepository
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
    private val userFollowsRepository: UserFollowsRepository
) : ViewModel() {

    private val _postResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val postResult : StateFlow<Resource<Boolean>> get() = _postResult

    private val _getAllPostResult = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val getAllPostResult : StateFlow<Resource<Boolean>> get() = _getAllPostResult

    private val _getAllPost = MutableStateFlow<Resource<Map<String,List<Post>>>>(Resource.Idle())
    val getAllPost : StateFlow<Resource<Map<String,List<Post>>>> get() = _getAllPost



    private val _postLikeResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val postLikeResult : StateFlow<Resource<Boolean>> get() = _postLikeResult

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

    fun addPost(emotion : String,content : String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = postRepository.addPost(emotion,content)
                _postResult.value = if (result.isSuccess){
                    Resource.Success(true)
                }else{
                    Resource.Failure("Post Atılamadı")
                }
            }catch (e :Exception){
                _postResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }

    private fun fetchPostsForAllUsers(userIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
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
                _getAllPost.value = Resource.Success(postsMap)

            } catch (e: Exception) {
                _getAllPost.value = Resource.Failure(e.message ?: "An error occurred")
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

    fun addLikedPost(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _postAddLikeResult.value = Resource.Loading()
                val postLikeResult = postLikesRepository.addLikeToPost(postId)
                if (postLikeResult.isFailure){
                    _postAddLikeResult.value = Resource.Failure("Beğeni eklenemedi")
                }else{
                    val userLikeResult = userLikedPostRepository.addLikePost(postId)
                    if (userLikeResult.isFailure){
                        postLikesRepository.removeLikeFromPost(postId)
                        _postResult.value = Resource.Failure("Beğeni eklenemedi")
                    }
                    else{
                        _postAddLikeResult.value = Resource.Success(true)
                    }
                }

            } catch (e: Exception) {
                // Hata durumunda hata mesajını güncelle
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
                        postCommentRepository.removeCommentFromPost(postId,commentId)
                        _postAddCommentResult.value = Resource.Failure("Kullanıcıya yorum eklenemedi")
                    } else {
                        _postAddCommentResult.value = Resource.Success(true)
                    }
                }
            } catch (e: Exception) {
                _postAddCommentResult.value = Resource.Failure(e.message ?: "Bir hata oluştu")
            }
        }
    }

    fun removeLikedPost(postId: String) {
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
                        _postRemoveLikeResult.value = Resource.Success(true)
                    }
                }

            } catch (e: Exception) {
                _postRemoveLikeResult.value = Resource.Failure(e.message ?: "Bir hata oluştu")
            }
        }
    }

    fun removeComment(postId: String, commentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _postRemoveCommentResult.value = Resource.Loading()
                val removeResult = postCommentRepository.removeCommentFromPost(postId, commentId)

                if (removeResult.isFailure) {
                    _postRemoveCommentResult.value = Resource.Failure("Yorum silinemedi")
                } else {
                    _postRemoveCommentResult.value = Resource.Success(true)
                }

            } catch (e: Exception) {
                _postRemoveCommentResult.value = Resource.Failure(e.message ?: "Bir hata oluştu")
            }
        }
    }

    fun fetchLikesCount(postId: String) {
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
                if (isLikedResult.isSuccess) {
                    val isLiked = isLikedResult.getOrNull() ?: false


                    _postLikeResult.value = if (isLiked) {
                        Resource.Success(true)//beğenilmiş
                    } else {
                        Resource.Success(false)//beğenilmemiş
                    }
                } else {
                    _postLikeResult.value = Resource.Failure("Beğeni durumu alınamadı")
                }
            } catch (e: Exception) {
                _postLikeResult.value = Resource.Failure(e.message ?: "Bir hata oluştu")
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