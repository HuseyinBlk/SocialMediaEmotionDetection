package com.hope.socialmediaemotiondetection.view

import GetUsernameScreen
import SearchScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.hope.socialmediaemotiondetection.model.user.User
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
                val navController = rememberNavController()
                NavHost(navController, startDestination =  "infoScreen" ) {
                    composable("mainScreen"){
                        MainScreen()
                    }
                    composable("infoScreen"){
                        InfoScreen(navController = navController)
                    }
                    composable("logInScreen"){
                        LoginScreen(navController = navController)
                    }
                    composable("registerScreen"){
                        RegistrationScreen(navController = navController)
                    }
                }
            }
        }
    }
}

