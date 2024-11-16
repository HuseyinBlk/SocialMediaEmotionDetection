package com.hope.socialmediaemotiondetection.utils

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
                result.isSuccess && result.getOrNull() != null
            } catch (e: Exception) {
                false
            }
        }
    }
}