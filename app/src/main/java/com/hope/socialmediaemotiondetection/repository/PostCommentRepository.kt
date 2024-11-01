package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hope.socialmediaemotiondetection.model.post.comment.Comment
import com.hope.socialmediaemotiondetection.model.post.comment.PostComments
import kotlinx.coroutines.tasks.await

class PostCommentRepository(
    private val auth : FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun addCommentToPost(postId: String, content: String, emotion: String): Result<Boolean> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))

        return try {
            val commentId = firestore.collection("posts").document(postId).collection("comments").document().id
            val comment = Comment(commentId, currentUser.uid, content, FieldValue.serverTimestamp(), emotion)

            val commentRef = firestore.collection("posts")
                .document(postId)
                .collection("comments")
                .document(commentId)

            commentRef.set(comment).await()

            firestore.collection("posts").document(postId)
                .update("commentsCount", FieldValue.increment(1)).await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeCommentFromPost(postId: String, commentId: String): Result<Boolean> {
        return try {
            firestore.collection("posts")
                .document(postId)
                .collection("comments")
                .document(commentId)
                .delete().await()

            firestore.collection("posts").document(postId)
                .update("commentsCount", FieldValue.increment(-1)).await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    /*EMOTİON GÜNCELLEME EKSİK MODEL GELSİN ONA GÖRE EKLİCEM*/
    suspend fun updateComment(postId: String, commentId: String, newContent: String): Result<Boolean> {
        return try {
            firestore.collection("posts")
                .document(postId)
                .collection("comments")
                .document(commentId)
                .update("content", newContent)
                .await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCommentsForPost(postId: String): Result<PostComments> {
        return try {
            val commentsList = mutableListOf<Comment>()
            val snapshot = firestore.collection("posts")
                .document(postId)
                .collection("comments")
                .get().await()

            for (document in snapshot.documents) {
                val comment = document.toObject(Comment::class.java)?.copy(commentId = document.id)
                if (comment != null) {
                    commentsList.add(comment)
                }
            }
            Result.success(PostComments(postId, commentsList))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}