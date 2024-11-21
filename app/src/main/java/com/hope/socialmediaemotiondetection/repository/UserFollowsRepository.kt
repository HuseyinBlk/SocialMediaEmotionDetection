package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/*
Takip/Takipçi işlemleri
 */

class UserFollowsRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun followUser(followedUserId: String): Result<Boolean> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))

        val followingRef = firestore.collection("users")
            .document(currentUser.uid)
            .collection("following")
            .document(followedUserId)

        val followerRef = firestore.collection("users")
            .document(followedUserId)
            .collection("followers")
            .document(currentUser.uid)

        val timestamp = FieldValue.serverTimestamp()

        return try {
            val batch = firestore.batch()
            batch.set(followingRef, mapOf("followedAt" to timestamp))
            batch.set(followerRef, mapOf("followerAt" to timestamp))
            batch.commit().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unfollowUser(followedUserId: String): Result<Boolean> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))

        // Takip edilen kullanıcının referansı
        val followingRef = firestore.collection("users")
            .document(currentUser.uid)
            .collection("following")
            .document(followedUserId)

        // Takip eden kullanıcının referansı
        val followerRef = firestore.collection("users")
            .document(followedUserId)
            .collection("followers")
            .document(currentUser.uid)

        return try {
            val batch = firestore.batch()
            batch.delete(followerRef)
            batch.delete(followingRef)
            batch.commit().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFollowingList(userId: String): Result<List<String>> {
        val followingRef = firestore.collection("users")
            .document(userId)
            .collection("following")

        return try {
            val followingDocs = followingRef.get().await()
            val followingList = followingDocs.map { it.id }
            Result.success(followingList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getFollowersList(userId: String): Result<List<String>> {
        val followersRef = firestore.collection("users")
            .document(userId)
            .collection("followers")

        return try {
            val followersDocs = followersRef.get().await()
            val followersList = followersDocs.map { it.id }
            Result.success(followersList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun isUserFollowing(followedUserId: String): Result<Boolean> {
        val currentUser = auth.currentUser ?: return Result.failure(Exception("User not logged in"))

        val followingRef = firestore.collection("users")
            .document(currentUser.uid)
            .collection("following")
            .document(followedUserId)

        return try {
            val doc = followingRef.get().await()
            Result.success(doc.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}