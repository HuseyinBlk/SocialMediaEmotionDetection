package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserLikedPostRepository (
    private val auth : FirebaseAuth,
    private val firestore: FirebaseFirestore
){


    suspend fun addLikePost(postId:String) : Result<Boolean>{
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val likedPost = firestore.collection("users")
            .document(currentUser.uid)
            .collection("likedPosts")
            .document(postId)

        val likedData = mapOf(
            "likedAt" to FieldValue.serverTimestamp() // Beğenilme zamanı
        )

        return try {
            likedPost.set(likedData).await()
            Result.success(true)
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    suspend fun getLikedPosts(): Result<List<String>>{
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val likedPostsRef = firestore.collection("users")
            .document(currentUser.uid)
            .collection("likedPosts")

        return try {
            val likedPostDocuments = likedPostsRef.get().await()
            val likedPostIds = likedPostDocuments.map { it.id }
            Result.success(likedPostIds)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeLikedPost(postId :String) :Result<Boolean>{
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))
        val likedPostRef = firestore.collection("users")
            .document(currentUser.uid)
            .collection("likedPosts")
            .document(postId)
        return try {
            likedPostRef.delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}