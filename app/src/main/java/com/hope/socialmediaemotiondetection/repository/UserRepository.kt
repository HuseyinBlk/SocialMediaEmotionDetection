package com.hope.socialmediaemotiondetection.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import com.hope.socialmediaemotiondetection.model.user.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    /*
    TRY CATCH EKLE UNUTMA
     */

    suspend fun registerUserDetails(username: String, profileImage : String? , bio: String? , interests: List<String?>?): Result<Boolean> {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val usernameCheckResult = isUsernameControl(username)
            if (usernameCheckResult.isSuccess && usernameCheckResult.getOrNull() == true) {
                println("Username already taken. Please choose a different username.")
                return Result.failure(Exception("Kullanıcı adı zaten alınmış."))
            }

            val userId = currentUser.uid
            val newUser = User(
                userId = userId,
                username = username,
                email = currentUser.email ?: "",
                profileImage = profileImage,
                bio = bio,
                interests = interests ?: emptyList()
            )

            val batch = firestore.batch()
            return try {
                saveUserToFireStore(batch, newUser)
                saveUsernameToFirestore(batch, username, userId)
                batch.commit().await()
                Result.success(true)
            } catch (e: Exception) {
                println("Error saving user details: ${e.message}")
                Result.failure(e)
            }
        } else {
            println("User is not logged in.")
            return Result.failure(Exception("User is not logged in."))
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

    private fun saveUsernameToFirestore(batch: WriteBatch, username: String, userId: String) {
        val usernameRef = firestore.collection("usernames").document(username)
        batch.set(usernameRef, mapOf("userId" to userId))
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

    suspend fun updateUser(username: String? = null, bio: String? = null, interests: List<String?>? = null) : Result<Boolean> {
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
                        return Result.failure(Exception("New username is already taken."))
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
                    return Result.success(true)
                }
                else{
                    return Result.failure(Exception("Unexpected error"))
                }
            }
            else {
                println("Error fetching user: ${result.exceptionOrNull()?.message}")
                return Result.failure(Exception("Error fetching user: ${result.exceptionOrNull()?.message}"))
            }
        }
        else {
            println("User is not logged in.")
            return Result.failure(Exception("User is not logged in."))
        }
    }

    private fun saveUserToFireStore(batch: WriteBatch, user: User) {
        val userRef = firestore.collection("users").document(user.userId)
        batch.set(userRef, user)
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


    /*
    Hata ihtimali
     */
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

    suspend fun getUserByIdFromFirestore(userId: String): Result<User?> {
        return try {
            // Directly fetch the document using the provided userId
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

    suspend fun getUsernameByUserId(userId: String): Result<String?> {
        return try {
            val userDocument = firestore.collection("users").document(userId).get().await()

            if (userDocument.exists()) {
                val username = userDocument.getString("username")
                if (username != null) {
                    Result.success(username)
                } else {
                    Result.failure(Exception("Username not found in user document"))
                }
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
