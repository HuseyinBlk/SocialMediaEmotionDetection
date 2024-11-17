package com.hope.socialmediaemotiondetection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.model.user.User
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
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userSearchResult = MutableStateFlow<Resource<List<User>>>(Resource.Idle())
    val userSearchResult: StateFlow<Resource<List<User>>> = _userSearchResult.asStateFlow()

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

}