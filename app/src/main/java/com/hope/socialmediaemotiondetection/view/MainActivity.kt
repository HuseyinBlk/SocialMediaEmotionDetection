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
import com.hope.socialmediaemotiondetection.view.Home.MainScreen
import com.hope.socialmediaemotiondetection.view.Info.InfoScreen
import com.hope.socialmediaemotiondetection.view.Login.LoginScreen
import com.hope.socialmediaemotiondetection.view.Loading.CheckUserNameScreen
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
            }
        }
    }
}

