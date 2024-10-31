package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthRepository (
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

    suspend fun loginUser(email: String,password: String):Result<Boolean>{
        return try {
            auth.signInWithEmailAndPassword(email,password)
            Result.success(true)
        }catch(e:Exception){
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