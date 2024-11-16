package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth : FirebaseAuth
) {

    suspend fun registerUser(email : String,password :String):Result<Boolean>{
        return try {
            auth.createUserWithEmailAndPassword(email,password).await()
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