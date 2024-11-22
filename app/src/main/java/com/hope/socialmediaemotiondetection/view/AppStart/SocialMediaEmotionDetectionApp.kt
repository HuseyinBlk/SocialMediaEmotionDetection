package com.hope.socialmediaemotiondetection.view.AppStart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hope.socialmediaemotiondetection.model.user.User
import com.hope.socialmediaemotiondetection.repository.AuthRepository
import com.hope.socialmediaemotiondetection.view.Home.MainScreen
import com.hope.socialmediaemotiondetection.view.Info.InfoScreen
import com.hope.socialmediaemotiondetection.view.Loading.CheckUserNameScreen
import com.hope.socialmediaemotiondetection.view.Login.LoginScreen
import com.hope.socialmediaemotiondetection.view.Profile.UserProfileScreen
import com.hope.socialmediaemotiondetection.view.Search.SearchScreen
import com.hope.socialmediaemotiondetection.view.registration.GetUsernameScreen
import com.hope.socialmediaemotiondetection.view.registration.RegistrationScreen
import com.hope.socialmediaemotiondetection.view.ui.theme.SocialMediaEmotionDetectionTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialMediaEmotionDetectionApp() {

    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    SocialMediaEmotionDetectionTheme {
        Scaffold(
            topBar = {

                when (currentRoute) {
                    "userProfileScreen" -> {
                        TopAppBar(
                            title = {},
                            modifier = Modifier.fillMaxHeight(0.08f),
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.White
                            ),
                            actions = {
                                // Özel butonlar eklenebilir
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings Icon",
                                    modifier = Modifier.padding(end = 5.dp)
                                )
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = "ExitToApp Icon",
                                    modifier = Modifier.padding(end = 5.dp)
                                        .clickable {
                                        // Initialize FirebaseAuth here
                                        val auth: FirebaseAuth = FirebaseAuth.getInstance()
                                        auth.signOut() // Sign out the user
                                        navController.navigate("infoScreen")
                                    }
                                )
                            }
                        )
                    }
                    else -> null // Diğer ekranlarda TopBar gösterilmez
                }

            },
            bottomBar = {
                if (currentRoute in listOf("searchScreen", "mainScreen","userProfileScreen")) {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                            label = { Text(text = "Home") },
                            selected = currentRoute == "mainScreen",
                            onClick = { navController.navigate("mainScreen") }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                            label = { Text(text = "Search") },
                            selected = currentRoute == "searchScreen",
                            onClick = { navController.navigate("searchScreen") }
                        )
                        NavigationBarItem(
                            icon = {Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")},
                            label= {Text(text = "Profile")},
                            selected = currentRoute == "userProfileScreen",
                            onClick = {navController.navigate("userProfileScreen")}
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "checkUserNameScreen",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("checkUserNameScreen") { CheckUserNameScreen(navController = navController) }
                composable("infoScreen") { InfoScreen(navController = navController) }
                composable("logInScreen") { LoginScreen(navController = navController) }
                composable("registerScreen") { RegistrationScreen(navController = navController) }
                composable("userDetailsScreen") { GetUsernameScreen(navController = navController) }
                composable("mainScreen") { MainScreen() }
                composable("searchScreen") { SearchScreen() }
                composable("userProfileScreen"){ UserProfileScreen() }
            }
        }
    }
}