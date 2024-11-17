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
import com.hope.socialmediaemotiondetection.view.loading.CheckUserNameScreen
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

