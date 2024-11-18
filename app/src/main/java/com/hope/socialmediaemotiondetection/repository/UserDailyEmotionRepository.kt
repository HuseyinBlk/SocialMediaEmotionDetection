package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hope.socialmediaemotiondetection.model.user.dailyEmotion.DailyEmotion
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class UserDailyEmotionRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
){

    private suspend fun createDailyEmotion(date: String): DailyEmotion {
        val newEmotionData = DailyEmotion(
            date = date,
            emotions = mapOf("sad" to 0, "happy" to 0 ,"love" to 0,"angry" to 0,"fear" to 0,"surprise" to 0),
            createdAt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
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
            val documentSnapshot = dailyEmotionRef.get().await()

            if (!documentSnapshot.exists()) {
                val newEmotionData = DailyEmotion(
                    date = date,
                    emotions = mapOf(
                        "sad" to 0,
                        "happy" to 0,
                        "love" to 0,
                        "angry" to 0,
                        "fear" to 0,
                        "surprise" to 0
                    ),
                    createdAt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                )
                dailyEmotionRef.set(newEmotionData).await()
            }

            val currentEmotionValue = documentSnapshot.get("emotions.$emotionType") as? Long ?: 0L
            val newEmotionValue = currentEmotionValue + increment

            dailyEmotionRef.update("emotions.$emotionType", newEmotionValue).await()

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