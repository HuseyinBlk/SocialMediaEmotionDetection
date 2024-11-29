package com.hope.socialmediaemotiondetection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hope.socialmediaemotiondetection.model.post.Post
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.model.user.User
import com.hope.socialmediaemotiondetection.model.user.comment.Comment
import com.hope.socialmediaemotiondetection.model.user.dailyEmotion.DailyEmotion
import com.hope.socialmediaemotiondetection.repository.AuthRepository
import com.hope.socialmediaemotiondetection.repository.PostCommentRepository
import com.hope.socialmediaemotiondetection.repository.PostRepository
import com.hope.socialmediaemotiondetection.repository.UserCommentsRepository
import com.hope.socialmediaemotiondetection.repository.UserDailyEmotionRepository
import com.hope.socialmediaemotiondetection.repository.UserFollowsRepository
import com.hope.socialmediaemotiondetection.repository.UserLikedPostRepository
import com.hope.socialmediaemotiondetection.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

/*
Profile detaylarının ekranın arka planını burada toparladık
 */

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val userLikedPostRepository: UserLikedPostRepository,
    private val userCommentsRepository: UserCommentsRepository,
    private val userFollowsRepository: UserFollowsRepository,
    private val postCommentRepository: PostCommentRepository,
    private val userDailyEmotionRepository: UserDailyEmotionRepository

) : ViewModel(){

    private val _removeCommentResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val removeCommentResult: StateFlow<Resource<Boolean>> get() = _removeCommentResult

    private val _userResult = MutableStateFlow<Resource<User?>>(Resource.Idle())
    val userResult: StateFlow<Resource<User?>> get() = _userResult

    private val _usernameResult = MutableStateFlow<Resource<String?>>(Resource.Idle())
    val usernameResult: StateFlow<Resource<String?>> get() = _usernameResult

    private val _postsResult = MutableStateFlow<Resource<List<Post>>>(Resource.Idle())
    val postsResult: StateFlow<Resource<List<Post>>> get() = _postsResult

    private val _postsRemoveResult = MutableStateFlow<Resource<String>>(Resource.Idle())
    val postsRemoveResult: StateFlow<Resource<String>> get() = _postsRemoveResult

    private val _likedPostsResult = MutableStateFlow<Resource<List<String>>>(Resource.Idle())
    val likedPostsResult: StateFlow<Resource<List<String>>> get() = _likedPostsResult

    private val _userCommentsResult = MutableStateFlow<Resource<Map<String, Comment>>>(Resource.Idle())
    val userCommentsResult: StateFlow<Resource<Map<String, Comment>>> get() = _userCommentsResult

    private val _followingListLengthResult = MutableStateFlow<Resource<String>>(Resource.Idle())
    val followingListLengthResult: StateFlow<Resource<String>> get() = _followingListLengthResult

    private val _followersListLengthResult = MutableStateFlow<Resource<String>>(Resource.Idle())
    val followersListLengthResult: StateFlow<Resource<String>> get() =_followersListLengthResult

    private val _dailyEmotionState = MutableStateFlow<Resource<DailyEmotion?>>(Resource.Idle())
    val dailyEmotionState: StateFlow<Resource<DailyEmotion?>> get() = _dailyEmotionState


    init {
        authRepository.currentUser()?.uid?.let {
            getPostsByUser(it)
            getUsernameByUserId(it)
            getFollowersList(it)
            getFollowingList(it)
            getUserComments(it)
            getDailyEmotion()
        } ?: run {

        }
    }
    fun removeComment(commentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _removeCommentResult.value = Resource.Loading()
            try {
                val result = userCommentsRepository.getUserComments(authRepository.currentUser()!!.uid)
                if (result.isSuccess) {
                    result.map { comments ->
                        var isDeleted = false
                        for ((_, comment) in comments) {
                            if (comment.commentId == commentId) {
                                println(comment.postId)
                                val endResult = postCommentRepository.removeCommentFromPost(postId = comment.postId!! , commentId = comment.commentId)
                                if (endResult.isSuccess) {
                                    println("Yorum başarıyla silindi: $commentId")
                                    isDeleted = true
                                    break
                                } else {
                                    println("Yorum silinemedi: $commentId")
                                }
                            }
                        }
                        if (isDeleted) {
                            _removeCommentResult.value = Resource.Success(true)
                        } else {
                            println("Yorum silinemedi: $commentId")
                            _removeCommentResult.value = Resource.Failure("Yorum silinemedi")
                        }
                    }
                } else {
                    println("getUserComments başarısız.")
                    _removeCommentResult.value = Resource.Failure("Yorum silinemedi")
                }
            } catch (e: Exception) {
                println("Yorum silme işleminde hata: ${e.message}")
                _removeCommentResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }

    fun getUserComments(userId: String = authRepository.currentUser()!!.uid) {
        viewModelScope.launch(Dispatchers.IO) {
            _userCommentsResult.value = Resource.Loading()
            try {
                val result = userCommentsRepository.getUserComments(userId)
                _userCommentsResult.value = if (result.isSuccess) {
                    Resource.Success(result.getOrNull().orEmpty())
                } else {
                    Resource.Failure("Kullanıcı yorumları alınamadı.")
                }
            } catch (e: Exception) {
                _userCommentsResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }



    fun getLikedPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _likedPostsResult.value = Resource.Loading()
            try {
                val result = userLikedPostRepository.getLikedPosts()
                _likedPostsResult.value = if (result.isSuccess) {
                    Resource.Success(result.getOrNull().orEmpty())
                } else {
                    Resource.Failure("Beğenilen gönderiler alınamadı.")
                }
            } catch (e: Exception) {
                _likedPostsResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }

    fun removePosts(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _postsRemoveResult.value = Resource.Loading() // Yükleniyor durumu

            try {
                val result = postRepository.removePost(postId)
                _postsRemoveResult.value = if (result.isSuccess) {
                    Resource.Success("Gönderi başarıyla silindi.")
                } else {
                    Resource.Failure("Gönderi silinirken bir hata oluştu: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                _postsRemoveResult.value = Resource.Failure("Bilinmeyen bir hata oluştu: ${e.message}")
            }
        }
    }

    fun getPostsByUser(userId: String = authRepository.currentUser()!!.uid) {
        viewModelScope.launch(Dispatchers.IO) {
            _postsResult.value = Resource.Loading()
            try {
                val result = postRepository.getPostsByUser(userId)
                _postsResult.value = if (result.isSuccess) {
                    Resource.Success(result.getOrNull().orEmpty())
                } else {
                    Resource.Failure("Gönderiler alınamadı.")
                }
            } catch (e: Exception) {
                _postsResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }

    fun formatTimestampToDate(timestamp: Any?): String {
        if (timestamp == null) return "Tarih bilinmiyor"
        return try {
            val millis = when (timestamp) {
                is com.google.firebase.Timestamp -> timestamp.toDate().time
                is Long -> timestamp
                else -> return "Geçersiz tarih"
            }
            val date = java.util.Date(millis)
            val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            formatter.format(date)
        } catch (e: Exception) {
            "Tarih formatlanamadı"
        }
    }

    fun getUserById(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _userResult.value = Resource.Loading()
            try {
                val result = userRepository.getUserByIdFromFirestore(userId)
                _userResult.value = if (result.isSuccess) {
                    Resource.Success(result.getOrNull())
                } else {
                    Resource.Failure("Kullanıcı bulunamadı.")
                }
            } catch (e: Exception) {
                _userResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }

    fun getUsernameByUserId(userId: String = authRepository.currentUser()!!.uid) {
        viewModelScope.launch(Dispatchers.IO) {
            _usernameResult.value = Resource.Loading()

            try {
                val result = userRepository.getUsernameByUserId(userId)

                _usernameResult.value = if (result.isSuccess) {
                    Resource.Success(result.getOrNull())
                } else {
                    Resource.Failure("Kullanıcı adı bulunamadı.")
                }
            } catch (e: Exception) {
                _usernameResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }

    fun getFollowingList(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _followingListLengthResult.value = Resource.Loading()
            try {
                val result = userFollowsRepository.getFollowingList(userId)
                _followingListLengthResult.value = if (result.isSuccess) {
                    val followingList = result.getOrNull().orEmpty()
                    Resource.Success(followingList.size.toString()) // Liste uzunluğunu String olarak döndürüyoruz
                } else {
                    Resource.Failure("Takip edilenler alınamadı.")
                }
            } catch (e: Exception) {
                _followingListLengthResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }


    fun getFollowersList(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _followersListLengthResult.value = Resource.Loading()
            try {
                val result = userFollowsRepository.getFollowersList(userId)
                _followersListLengthResult.value = if (result.isSuccess) {
                    val followersList = result.getOrNull().orEmpty()
                    Resource.Success(followersList.size.toString()) // Liste uzunluğunu String olarak döndürüyoruz
                } else {
                    Resource.Failure("Takipçiler alınamadı.")
                }
            } catch (e: Exception) {
                _followersListLengthResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }

    fun getDailyEmotion() {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        viewModelScope.launch(Dispatchers.IO) {
            _dailyEmotionState.value = Resource.Loading()

            val result = userDailyEmotionRepository.getDailyEmotion(today)

            _dailyEmotionState.value = if (result.isSuccess) {
                val emotion = result.getOrNull()
                if (emotion != null) {
                    Resource.Success(emotion)
                } else {
                    Resource.Success(null)
                }
            } else {
                Resource.Failure(result.exceptionOrNull()?.message ?: "Bilinmeyen bir hata oluştu")
            }
        }
    }
}