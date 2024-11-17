package com.hope.socialmediaemotiondetection.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserNameCheckRepository (private val firestore: FirebaseFirestore) {
    suspend fun getUsernameByUserId(userId: String): Result<String?> {
        return try {
            val querySnapshot = firestore.collection("usernames")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val username = document.id
                Result.success(username)
            } else {
                Result.failure(Exception("Username not found for userId: $userId"))
            }
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error fetching username for userId $userId: ${e.message}", e)
            Result.failure(e)
        }
    }
}