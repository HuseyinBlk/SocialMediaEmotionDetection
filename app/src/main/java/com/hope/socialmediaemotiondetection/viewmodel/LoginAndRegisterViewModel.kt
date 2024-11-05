package com.hope.socialmediaemotiondetection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginAndRegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _registerResult = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val registerResult: StateFlow<Resource<Boolean>> get() = _registerResult

    private val _loginResult = MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val loginResult: StateFlow<Resource<Boolean>> get() = _loginResult
    /*
    StateFlow sadece okuma yapabilirsin dikkat et
    Mutable anlık güncellicek
     */

    fun register(email: String, password: String) {
        viewModelScope.launch (Dispatchers.IO){
            _registerResult.value = Resource.Loading()
            try {
                val result = authRepository.registerUser(email, password)
                _registerResult.value = if (result.isSuccess) {
                    Resource.Success(true)
                } else {
                    Resource.Failure("Kayıt başarısız.")
                }
            } catch (e: Exception) {
                _registerResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch (Dispatchers.IO){
            _loginResult.value = Resource.Loading()
            try {
                val result = authRepository.loginUser(email, password)
                _loginResult.value = if (result.isSuccess) {
                    Resource.Success(true)
                } else {
                    Resource.Failure("Giriş başarısız.")
                }
            } catch (e: Exception) {
                _loginResult.value = Resource.Failure(e.message ?: "Bilinmeyen bir hata oluştu.")
            }
        }
    }
}
