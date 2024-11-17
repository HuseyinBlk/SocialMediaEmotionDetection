package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth : FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun registerUser(email : String,password :String):Result<Boolean>{
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("Kullanıcı kimliği alınamadı")

            val userDocument = mapOf(
                "username" to "",
                "email" to email,
                "profileImage" to "",
                "bio" to "",
                "interests" to emptyList<String>()
            )

            firestore.collection("users").document(userId).set(userDocument).await()
            Result.success(true)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    suspend fun loginUser(email: String, password: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            val user = auth.currentUser
            if (user != null) {
                Result.success(true)
            } else {
                Result.failure(Exception("Kullanıcı bulunamadı"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logoutUser(){
        auth.signOut()
    }

    fun currentUser() : FirebaseUser? {
        return auth.currentUser
    }

}