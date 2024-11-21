package com.hope.socialmediaemotiondetection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hope.socialmediaemotiondetection.model.post.Post
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.model.user.User
import com.hope.socialmediaemotiondetection.model.user.comment.Comment
import com.hope.socialmediaemotiondetection.model.user.dailyEmotion.DailyEmotion
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
import javax.inject.Inject

/*
Profile detaylarının ekranın arka planını burada toparladık
 */

@HiltViewModel
class ProfileDetailsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val userLikedPostRepository: UserLikedPostRepository,
    private val userCommentsRepository: UserCommentsRepository,
    private val userFollowsRepository: UserFollowsRepository,
    private val userDailyEmotionRepository: UserDailyEmotionRepository

) : ViewModel(){

    private val _userResult = MutableStateFlow<Resource<User?>>(Resource.Idle())
    val userResult: StateFlow<Resource<User?>> get() = _userResult

    private val _usernameResult = MutableStateFlow<Resource<String?>>(Resource.Idle())
    val usernameResult: StateFlow<Resource<String?>> get() = _usernameResult

    private val _postsResult = MutableStateFlow<Resource<List<Post>>>(Resource.Idle())
    val postsResult: StateFlow<Resource<List<Post>>> get() = _postsResult

    private val _likedPostsResult = MutableStateFlow<Resource<List<String>>>(Resource.Idle())
    val likedPostsResult: StateFlow<Resource<List<String>>> get() = _likedPostsResult

    private val _userCommentsResult = MutableStateFlow<Resource<Map<String, Comment>>>(Resource.Idle())
    val userCommentsResult: StateFlow<Resource<Map<String, Comment>>> get() = _userCommentsResult

    private val _followingListResult = MutableStateFlow<Resource<List<String>>>(Resource.Idle())
    val followingListResult: StateFlow<Resource<List<String>>> get() = _followingListResult

    private val _followersListResult = MutableStateFlow<Resource<List<String>>>(Resource.Idle())
    val followersListResult: StateFlow<Resource<List<String>>> get() = _followersListResult

    private val _dailyEmotionState = MutableStateFlow<Resource<DailyEmotion?>>(Resource.Idle())
    val dailyEmotionState: StateFlow<Resource<DailyEmotion?>> get() = _dailyEmotionState


    fun getUserComments(userId: String) {
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

    fun getPostsByUser(userId: String) {
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

    fun getUsernameByUserId(userId: String) {
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
            _followingListResult.value = Resource.Loading()
            try {
                val result = userFollowsRepository.getFollowingList(userId)
                _followingListResult.value = if (result.isSuccess) {
                    Resource.Success(result.getOrNull().orEmpty())
                } else {
                    Resource.Failure("Takip edilenler alınamadı.")
                }
            } catch (e: Exception) {
                _followingListResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }

    fun getFollowersList(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _followersListResult.value = Resource.Loading()
            try {
                val result = userFollowsRepository.getFollowersList(userId)
                _followersListResult.value = if (result.isSuccess) {
                    Resource.Success(result.getOrNull().orEmpty())
                } else {
                    Resource.Failure("Takipçiler alınamadı.")
                }
            } catch (e: Exception) {
                _followersListResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }

    fun getDailyEmotion(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _dailyEmotionState.value = Resource.Loading()

            val result = userDailyEmotionRepository.getDailyEmotion(date)

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