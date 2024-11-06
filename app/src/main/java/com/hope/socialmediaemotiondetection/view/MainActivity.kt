package com.hope.socialmediaemotiondetection.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
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
            SocialMediaEmotionDetectionTheme {
                val firebaseAuth = FirebaseAuth.getInstance()
                val navController = rememberNavController()
                NavHost(navController, startDestination = if (firebaseAuth.currentUser != null) "mainScreen" else "infoScreen" ) {
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

