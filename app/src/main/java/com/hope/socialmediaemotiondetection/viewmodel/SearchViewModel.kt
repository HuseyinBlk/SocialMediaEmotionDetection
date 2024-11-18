package com.hope.socialmediaemotiondetection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.model.user.User
import com.hope.socialmediaemotiondetection.repository.UserFollowsRepository
import com.hope.socialmediaemotiondetection.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userFollowsRepository: UserFollowsRepository
) : ViewModel() {

    private val _userSearchResult = MutableStateFlow<Resource<List<User>>>(Resource.Idle())
    val userSearchResult: StateFlow<Resource<List<User>>> = _userSearchResult.asStateFlow()

    private val _followResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val followResult: StateFlow<Resource<Boolean>> = _followResult.asStateFlow()

    private val _unfollowResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val unfollowResult: StateFlow<Resource<Boolean>> = _unfollowResult.asStateFlow()

    private val _followCheckResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val followCheckResult: StateFlow<Resource<Boolean>> = _followCheckResult.asStateFlow()


    fun searchUsersByUsername(username: String) {
        viewModelScope.launch (Dispatchers.IO) {
            if (username.isBlank()){
                _userSearchResult.value = Resource.Idle()
            }else{
                _userSearchResult.value = Resource.Loading()
                val result = userRepository.getUserNamesSimilarTo(username)
                _userSearchResult.value = result
            }
        }
    }

    fun followUser(followedUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _followResult.value = Resource.Loading()
            val result = userFollowsRepository.followUser(followedUserId)
            if (result.isSuccess) {
                _followResult.value = Resource.Success(true)
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                _followResult.value = Resource.Failure(errorMessage)
            }
        }
    }

    fun unfollowUser(followedUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _unfollowResult.value = Resource.Loading()
            val result = userFollowsRepository.unfollowUser(followedUserId)
            if (result.isSuccess) {
                _unfollowResult.value = Resource.Success(true)
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                _unfollowResult.value = Resource.Failure(errorMessage)
            }
        }
    }

    fun checkIfUserIsFollowing(followedUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _followCheckResult.value = Resource.Loading()

            val result = userFollowsRepository.isUserFollowing(followedUserId)

            if (result.isSuccess) {
                _followCheckResult.value = Resource.Success(result.getOrNull() ?: false)
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                _followCheckResult.value = Resource.Failure(errorMessage)
            }
        }
    }

}