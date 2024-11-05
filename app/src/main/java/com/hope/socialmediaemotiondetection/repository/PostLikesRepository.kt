package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hope.socialmediaemotiondetection.model.post.likes.Like
import com.hope.socialmediaemotiondetection.model.post.likes.PostLikes
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostLikesRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun addLikeToPost(postId: String): Result<Boolean> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val likeId = currentUser.uid
        val like = Like(userId = currentUser.uid, likedAt = FieldValue.serverTimestamp())

        return try {
            val batch = firestore.batch()

            val likeRef = firestore.collection("posts").document(postId)
                .collection("likes").document(likeId)
            batch.set(likeRef, like)

            val postRef = firestore.collection("posts").document(postId)
            batch.update(postRef, "likesCount", FieldValue.increment(1))

            batch.commit().await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeLikeFromPost(postId: String): Result<Boolean> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val likeId = currentUser.uid

        return try {
            firestore.collection("posts").document(postId)
                .collection("likes").document(likeId).delete().await()

            firestore.collection("posts").document(postId)
                .update("likesCount", FieldValue.increment(-1)).await()

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLikesCount(postId: String): Result<Int> {
        return try {
            val snapshot = firestore.collection("posts").document(postId).get().await()
            val likesCount = snapshot.getLong("likesCount")?.toInt() ?: 0
            Result.success(likesCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isPostLikedByUser(postId: String): Result<Boolean> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val likeId = currentUser.uid

        return try {
            val snapshot = firestore.collection("posts").document(postId)
                .collection("likes").document(likeId).get().await()

            Result.success(snapshot.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPostLikes(postId: String): Result<PostLikes> {
        return try {
            val likesSnapshot = firestore.collection("posts")
                .document(postId)
                .collection("likes")
                .get()
                .await()

            val likes = mutableListOf<Like>()
            for (document in likesSnapshot.documents) {
                val like = document.toObject(Like::class.java)
                like?.let { likes.add(it) }
            }

            val postLikes = PostLikes(postId = postId, likes = likes)
            Result.success(postLikes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}