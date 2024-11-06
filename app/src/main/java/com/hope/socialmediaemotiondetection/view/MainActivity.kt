package com.hope.socialmediaemotiondetection.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hope.socialmediaemotiondetection.view.Home.MainScreen
import com.hope.socialmediaemotiondetection.view.ui.theme.SocialMediaEmotionDetectionTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocialMediaEmotionDetectionTheme {
                /*val firebaseAuth = FirebaseAuth.getInstance()
                val navController = rememberNavController()
                NavHost(navController, startDestination = if (firebaseAuth.currentUser != null) "mainScreen" else "infoScreen" ) {
                    composable("mainScreen"){

                    }
                    composable("infoScreen"){
                        InfoScreen(navController = navController)
                    }
                    composable("logInScreen"){
                        LoginScreen()
                    }
                    composable("registerScreen"){
                        RegistrationScreen()
                    }

                 */
                MainScreen()

            }
        }
    }
}

