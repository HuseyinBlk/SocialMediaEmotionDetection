package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hope.socialmediaemotiondetection.model.user.User
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    /*
    TRY CATCH EKLE UNUTMA
     */

    suspend fun registerUserDetails(username: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val usernameCheckResult = isUsernameControl(username)
            if (usernameCheckResult.isSuccess && usernameCheckResult.getOrNull() == true) {
                println("Username already taken. Please choose a different username.")
                return
            }

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
            saveUsernameToFirestore(username, userId)
        } else {
            println("User is not logged in.")
        }
    }

    private suspend fun isUsernameControl(username: String): Result<Boolean> {
        return try {
            val document = firestore.collection("usernames").document(username).get().await()
            Result.success(document.exists())
        } catch (e: Exception) {
            println("Error checking username availability: ${e.message}")
            Result.failure(e)
        }
    }

    private suspend fun saveUsernameToFirestore(username: String, userId: String) {
        try {
            firestore.collection("usernames").document(username).set(mapOf("userId" to userId)).await()
        } catch (e: Exception) {
            println("Error saving username: ${e.message}")
        }
    }

    private suspend fun updateUsernameInFirestore(oldUsername: String, newUsername: String, userId: String) {
        try {
            val batch = firestore.batch()
            val oldUsernameRef = firestore.collection("usernames").document(oldUsername)
            batch.delete(oldUsernameRef)
            val newUsernameRef = firestore.collection("usernames").document(newUsername)
            batch.set(newUsernameRef, mapOf("userId" to userId))
            batch.commit().await()
        } catch (e: Exception) {
            println("Error updating username: ${e.message}")
        }
    }

    suspend fun updateUser(username: String? = null, bio: String? = null, interests: List<String?>? = null) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val result = getUserFromFirestore(userId)

            if (result.isSuccess) {
                val existingUser = result.getOrNull()
                if (existingUser != null) {
                    val updatedUsername = username ?: existingUser.username
                    if (updatedUsername != existingUser.username && isUsernameControl(updatedUsername).isSuccess) {
                        println("New username is already taken.")
                        return
                    }

                    val updatedUser = User(
                        userId = userId,
                        username = updatedUsername,
                        email = currentUser.email ?: "",
                        profileImage = existingUser.profileImage,
                        bio = bio ?: existingUser.bio,
                        interests = interests ?: existingUser.interests
                    )

                    updateUserInFirestore(updatedUser)
                }
            } else {
                println("Error fetching user: ${result.exceptionOrNull()?.message}")
            }
        } else {
            println("User is not logged in.")
        }
    }

    private suspend fun saveUserToFireStore(user: User) {
        try {
            firestore.collection("users").document(user.userId).set(user).await()
        } catch (e: Exception) {
            println("Error saving user: ${e.message}")
        }
    }

    private suspend fun updateUserInFirestore(user: User) {
        try {
            firestore.collection("users").document(user.userId).set(user).await()
        } catch (e: Exception) {
            println("Error updating user: ${e.message}")
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

    suspend fun getUserNameFromFirestore(username: String): Result<User?> {
        return try {
            val usernameDocument = firestore.collection("usernames").document(username).get().await()

            if (usernameDocument.exists()) {
                val userId = usernameDocument.getString("userId")
                if (userId != null) {
                    // Kullanıcı ID'sini kullanarak kullanıcıyı al
                    val userDocument = firestore.collection("users").document(userId).get().await()
                    if (userDocument.exists()) {
                        Result.success(userDocument.toObject(User::class.java))
                    } else {
                        Result.failure(Exception("User not found"))
                    }
                } else {
                    Result.failure(Exception("User ID not found in username document"))
                }
            } else {
                Result.failure(Exception("Username not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
