package com.hope.socialmediaemotiondetection.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hope.socialmediaemotiondetection.view.AppStart.SocialMediaEmotionDetectionApp

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            /*
            SocialMediaEmotionDetectionTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "checkUserNameScreen") {
                    composable("checkUserNameScreen") {
                        CheckUserNameScreen(navController)
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

                    composable("searchScreen") {
                        SearchScreen()
                    }
                }
            }*/
            SocialMediaEmotionDetectionApp()
        }
    }
}

