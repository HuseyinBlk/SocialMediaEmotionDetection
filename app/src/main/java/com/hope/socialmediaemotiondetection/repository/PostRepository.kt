package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hope.socialmediaemotiondetection.model.post.Post
import kotlinx.coroutines.tasks.await

class PostRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun addPost(emotion : String,content : String) : Result<Boolean>{
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val postId = firestore.collection("posts").document().id
        val newPost = Post(
            postId = postId,
            content = content,
            createdAt = FieldValue.serverTimestamp(),
            userId = currentUser.uid,
            emotion = emotion
        )
        return try {
            firestore.collection("posts").document(postId).set(newPost).await()
            Result.success(true)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun getPostsByUser(userId: String): Result<List<Post>> {
        return try {
            val documentSnapshot = firestore.collection("posts")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val posts = documentSnapshot.documents.mapNotNull { document ->
                document.toObject(Post::class.java)?.copy(postId = document.id)
            }

            Result.success(posts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removePost(postId: String): Result<Boolean> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))

        return try {
            val postDocument = firestore.collection("posts").document(postId).get().await()
            if (!postDocument.exists()) {
                return Result.failure(Exception("Post not found"))
            }
            val postUserId = postDocument.getString("userId") ?: return Result.failure(Exception("Post userId not found"))
            if (postUserId != currentUser.uid) {
                return Result.failure(Exception("You do not have permission to delete this post"))
            }

            firestore.collection("posts").document(postId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}