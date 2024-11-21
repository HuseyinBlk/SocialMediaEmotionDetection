package com.hope.socialmediaemotiondetection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
Kayıt olduktan sonraki çıkan Detaylar sınıfının arka plan kodları
 */

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _updateUserResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val updateUserResult: StateFlow<Resource<Boolean>> get() = _updateUserResult

    private val _registerUserResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle())
    val registerUserResult : StateFlow<Resource<Boolean>> get() = _registerUserResult

    fun updateUser(username: String? = null, bio: String? = null, interests: List<String?>? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserResult.value = Resource.Loading()
            try {
                val result = userRepository.updateUser(username, bio, interests)
                _updateUserResult.value = if (result.isSuccess) {
                    Resource.Success(true)
                } else {
                    Resource.Failure("Lütfen bilgilerinizi kontorol ediniz")
                }
            } catch (e: Exception) {
                _updateUserResult.value = Resource.Failure(e.message ?: "Error")
            }
        }
    }

    fun registerUserDetails(username: String, profileImage : String? , bio: String? , interests: List<String?>?){
        viewModelScope.launch(Dispatchers.IO) {
            _registerUserResult.value = Resource.Loading()
            try {
                val result = userRepository.registerUserDetails(username,profileImage,bio,interests)
                _registerUserResult.value = if (result.isSuccess) {
                    Resource.Success(true)
                } else {
                    Resource.Failure("Lütfen bilgilerinizi kontorol ediniz")
                }
            }catch (e: Exception){
                _registerUserResult.value = Resource.Failure(e.message ?: "Error")
            }

        }
    }

}