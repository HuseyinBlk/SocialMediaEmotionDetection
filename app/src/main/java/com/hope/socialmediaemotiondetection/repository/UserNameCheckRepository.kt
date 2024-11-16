package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserNameCheckRepository (private val firestore: FirebaseFirestore) {
    suspend fun getUsernameByUserId(userId: String): Result<String?> {
        return try {
            val userDocument = firestore.collection("users").document(userId).get().await()

            if (userDocument.exists()) {
                val username = userDocument.getString("username")
                if (username != null) {
                    Result.success(username)
                } else {
                    Result.failure(Exception("Username not found in user document"))
                }
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}