package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hope.socialmediaemotiondetection.model.user.dailyEmotion.DailyEmotion
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDailyEmotionRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
){

    private suspend fun createDailyEmotion(date: String): DailyEmotion {
        val newEmotionData = DailyEmotion(
            date = date,
            emotions = mapOf("happy" to 0, "sadness" to 0 /*DEVAMINI UNUTMA*/),
            createdAt = FieldValue.serverTimestamp().toString()
        )

        val currentUser = auth.currentUser ?: throw Exception("User not logged in")
        val dailyEmotionRef = firestore.collection("users")
            .document(currentUser.uid)
            .collection("dailyEmotions")
            .document(date)

        dailyEmotionRef.set(newEmotionData).await()
        return newEmotionData
    }

    suspend fun updateDailyEmotion(date: String, emotionType: String, increment: Int): Result<Boolean> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))

        val dailyEmotionRef = firestore.collection("users")
            .document(currentUser.uid)
            .collection("dailyEmotions")
            .document(date)

        return try {
            dailyEmotionRef.update("emotions.$emotionType", FieldValue.increment(increment.toLong())).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /*
    suspend fun updateDailyEmotion(date: String, emotionType: String, increment: Int): Result<Boolean> {
    val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))

    val dailyEmotionResult = getDailyEmotion(date)
    if (dailyEmotionResult.isFailure) {
        return Result.failure(dailyEmotionResult.exceptionOrNull() ?: Exception("Failed to get daily emotion"))
    }

    val dailyEmotion = dailyEmotionResult.getOrNull() ?: return Result.failure(Exception("Failed to create or retrieve daily emotion"))

    val currentEmotionValue = dailyEmotion.emotions[emotionType] ?: 0
    val newEmotionValue = currentEmotionValue + increment

    val dailyEmotionRef = firestore.collection("users")
        .document(currentUser.uid)
        .collection("dailyEmotions")
        .document(date)

    return try {
        // Manuel olarak yeni değeri ayarla
        dailyEmotionRef.update("emotions.$emotionType", newEmotionValue).await()
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
     */

    suspend fun getDailyEmotion(date: String): Result<DailyEmotion?> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val dailyEmotionRef = firestore.collection("users")
            .document(currentUser.uid)
            .collection("dailyEmotions")
            .document(date)

        return try {
            val dailyEmotionDoc = dailyEmotionRef.get().await()
            if (dailyEmotionDoc.exists()) {
                Result.success(dailyEmotionDoc.toObject(DailyEmotion::class.java))
            } else {
                val newEmotion = createDailyEmotion(date)
                Result.success(newEmotion)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}