package com.hope.socialmediaemotiondetection.view

import GetUsernameScreen
import SearchScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hope.socialmediaemotiondetection.model.user.User
import com.hope.socialmediaemotiondetection.repository.UserNameCheckRepository
import com.hope.socialmediaemotiondetection.utils.CheckUserName
import com.hope.socialmediaemotiondetection.view.Home.MainScreen
import com.hope.socialmediaemotiondetection.view.Info.InfoScreen
import com.hope.socialmediaemotiondetection.view.Login.LoginScreen
import com.hope.socialmediaemotiondetection.view.registration.RegistrationScreen
import com.hope.socialmediaemotiondetection.view.ui.theme.SocialMediaEmotionDetectionTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userList = listOf(
                User(
                    userId = "1",
                    username = "Hüseyin Balık",
                    email = "huseyin@example.com",
                    profileImage = "https://example.com/profile1.jpg",
                    bio = "Software Engineer",
                    interests = listOf("Tech", "AI")
                ),
                User(
                    userId = "2",
                    username = "Ahmet Yılmaz",
                    email = "ahmet@example.com",
                    profileImage = "https://example.com/profile2.jpg",
                    bio = "Graphic Designer",
                    interests = listOf("Art", "Photography")
                ),
                User(
                    userId = "3",
                    username = "Mehmet Öz",
                    email = "mehmet@example.com",
                    profileImage = null, // Profil resmi olmayan kullanıcı
                    bio = "Doctor and Writer",
                    interests = listOf("Health", "Writing")
                ),
                User(
                    userId = "4",
                    username = "Selin Kaya",
                    email = "selin@example.com",
                    profileImage = "https://example.com/profile4.jpg",
                    bio = "Music Lover",
                    interests = listOf("Music", "Travel")
                ),
                User(
                    userId = "5",
                username = "Hüseyin Balık",
                email = "huseyin@example.com",
                profileImage = "https://example.com/profile1.jpg",
                bio = "Software Engineer",
                interests = listOf("Tech", "AI")
                )
            )
            SocialMediaEmotionDetectionTheme {
                val firebaseAuth = FirebaseAuth.getInstance()
                val firestore = FirebaseFirestore.getInstance()
                val navController = rememberNavController()


                NavHost(navController = navController, startDestination = if (firebaseAuth.currentUser != null) "checkUserNameScreen" else "infoScreen") {
                    composable("checkUserNameScreen") {
                        val isLoading = remember { mutableStateOf(true) }
                        LaunchedEffect(Unit) {
                            val checkUserName = CheckUserName(userNameCheck = UserNameCheckRepository(firestore))
                            val isAvailable = checkUserName.isUserNameAvailable(firebaseAuth.currentUser!!.uid)
                            isLoading.value = false
                            if (isAvailable) {
                                navController.navigate("mainScreen") {
                                    popUpTo("checkUserNameScreen") { inclusive = true }
                                }
                            } else {
                                navController.navigate("userDetailsScreen") {
                                    popUpTo("checkUserNameScreen") { inclusive = true }
                                }
                            }
                        }

                        // Eğer yükleme durumu varsa, CircularProgressIndicator gösteriyoruz
                        if (isLoading.value) {
                            // Yükleme göstergesi (CircularProgressIndicator)
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    // InfoScreen
                    composable("infoScreen") {
                        InfoScreen(navController = navController)
                    }

                    // LogInScreen
                    composable("logInScreen") {
                        LoginScreen(navController = navController)
                    }

                    // RegisterScreen
                    composable("registerScreen") {
                        RegistrationScreen(navController = navController)
                    }

                    // UserDetailsScreen
                    composable("userDetailsScreen") {
                        GetUsernameScreen(navController = navController)
                    }

                    // MainScreen (Bu ekran yalnızca kullanıcı adı kontrolü sonrası gösterilecek)
                    composable("mainScreen") {
                        MainScreen(navController = navController)
                    }
                }
            }
        }
    }
}

