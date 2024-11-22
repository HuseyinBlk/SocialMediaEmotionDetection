package com.hope.socialmediaemotiondetection.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hope.socialmediaemotiondetection.model.user.comment.Comment
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/*
User yorum ekleme işlemeleri burada
 */

class UserCommentsRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun addCommentToUser(postId: String, content: String, emotion: String) : Result<Boolean> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val commentId = firestore.collection("users").document(currentUser.uid)
            .collection("comments").document().id
        val comment = Comment(
            commentId,
            postId,
            content,
            FieldValue.serverTimestamp(),
            emotion
        )

        return try {
            firestore.collection("users").document(currentUser.uid)
                .collection("comments").document(commentId).set(comment).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeCommentFromUser(commentId: String): Result<Boolean> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))

        return try {
            firestore.collection("users")
                .document(currentUser.uid)
                .collection("comments")
                .document(commentId)
                .delete()
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserComments(userId: String): Result<Map<String, Comment>> {
        return try {
            val commentsSnapshot = firestore.collection("users").document(userId)
                .collection("comments").get().await()

            // Yorumları id ile birliktedöndür
            val commentsMap = commentsSnapshot.documents.associate { doc ->
                val comment = Comment(
                    commentId =  doc.getString("commentId") ?: "",
                    postId = doc.getString("postId") ?: "",
                    content = doc.getString("content") ?: "",
                    createdAt = doc.get("createdAt") ?: "",
                    emotion = doc.getString("emotion") ?: ""
                )
                doc.id to comment
            }

            Result.success(commentsMap)
        } catch (e: Exception) {
            Result.failure(Exception("Failed to fetch user comments: ${e.message}"))
        }
    }
}
