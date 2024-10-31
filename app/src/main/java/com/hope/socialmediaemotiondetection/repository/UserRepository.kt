package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hope.socialmediaemotiondetection.model.user.User
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth : FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    /*
    TRY CATCH EKLE UNUTMA
     */


    suspend fun registerUserDetails(username: String){
        val currentUser = auth.currentUser
        if (currentUser != null){
            val userId = currentUser.uid

            val newUser = User(
                userId = userId,
                username = username,
                email = currentUser.email ?: "",
                profileImage = null,
                bio = null,
                interests = emptyList()
            )
            saveUserToFireStore(newUser)
        }else{
            //Hata kontrolü
        }
    }

    suspend fun updateUser(username: String? = null, bio: String? = null, interests: List<String?>? = null) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            val result = getUserFromFirestore(userId)

            if (result.isSuccess) {
                val existingUser = result.getOrNull() // Kullanıcıyı al

                if (existingUser != null) {
                    val updatedUser = User(
                        userId = userId,
                        username = username ?: existingUser.username,
                        email = currentUser.email ?: "",
                        profileImage = existingUser.profileImage,
                        bio = bio ?: existingUser.bio,
                        interests = interests ?: existingUser.interests
                    )

                    // Kullanıcıyı Firestore'da güncelle
                    updateUserInFirestore(updatedUser)
                }
            } else {
                // Hata durumu bulamazsa veya başka bir hata olursa
                println("Error fetching user: ${result.exceptionOrNull()?.message}")
            }
        } else {
            // Kullanıcı giriş yapmamış
        }
    }


    private suspend fun saveUserToFireStore(user: User) {
        try {
            firestore.collection("users").document(user.userId).set(user).await()
        } catch (e: Exception) {
            println(e)
        }
    }
    private suspend fun updateUserInFirestore(user: User) {
        try {
            firestore.collection("users").document(user.userId).set(user).await()
        } catch (e: Exception) {
            // Hata yönetimi
        }
    }

    private suspend fun getUserFromFirestore(userId: String): Result<User?> {
        return try {
            val userDocument = firestore.collection("users").document(userId).get().await()
            if (userDocument.exists()) {
                Result.success(userDocument.toObject(User::class.java))
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}