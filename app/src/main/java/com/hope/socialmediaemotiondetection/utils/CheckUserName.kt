package com.hope.socialmediaemotiondetection.utils

import android.util.Log
import com.hope.socialmediaemotiondetection.repository.UserNameCheckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/*
Utils tek seferlik kontroller için kullanılıyor bunu ayrı tuttum
kullanıcı tek seferlik Username kullanılabilirliğini kontrol ediyorum
*/

class CheckUserName @Inject constructor(
    private val userNameCheck: UserNameCheckRepository
) {

    suspend fun isUserNameAvailable(userId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val result = userNameCheck.getUsernameByUserId(userId)
                return@withContext when {
                    result.isSuccess -> {
                        result.getOrNull() != ""
                    }
                    result.isFailure -> {
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