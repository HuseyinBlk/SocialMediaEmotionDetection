package com.hope.socialmediaemotiondetection.utils

import android.util.Log
import com.hope.socialmediaemotiondetection.repository.UserNameCheckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckUserName @Inject constructor(
    private val userNameCheck: UserNameCheckRepository
) {

    suspend fun isUserNameAvailable(userId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val result = userNameCheck.getUsernameByUserId(userId)
                return@withContext when {
                    result.isSuccess -> {
                        // Eğer username varsa (null değilse), username zaten var, o yüzden false döndürüyoruz
                        result.getOrNull() != ""
                    }
                    result.isFailure -> {
                        // Eğer hata varsa, false döndürüyoruz
                        Log.e("UsernameCheck", "Error: ${result.exceptionOrNull()}")
                        false
                    }
                    else -> false
                }
            } catch (e: Exception) {
                // Hata durumunda logluyoruz ve false döndürüyoruz
                Log.e("UsernameCheck", "Error while checking username: ${e.message}", e)
                return@withContext false
            }
        }
    }
}