package com.hope.socialmediaemotiondetection.view.AppStart

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hope.socialmediaemotiondetection.view.Home.MainScreen
import com.hope.socialmediaemotiondetection.view.Info.InfoScreen
import com.hope.socialmediaemotiondetection.view.Loading.CheckUserNameScreen
import com.hope.socialmediaemotiondetection.view.Login.LoginScreen
import com.hope.socialmediaemotiondetection.view.Search.SearchScreen
import com.hope.socialmediaemotiondetection.view.registration.GetUsernameScreen
import com.hope.socialmediaemotiondetection.view.registration.RegistrationScreen
import com.hope.socialmediaemotiondetection.view.ui.theme.SocialMediaEmotionDetectionTheme


@Composable
fun SocialMediaEmotionDetectionApp() {

    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    SocialMediaEmotionDetectionTheme {
        Scaffold(
            topBar = {
                /*
                when (currentRoute) {
                    "searchScreen" -> {
                        TopAppBar(
                            title = { Text(text = "Arama Ekranı") },
                            actions = {
                                // Özel butonlar eklenebilir
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Icon"
                                )
                            }
                        )
                    }
                    "mainScreen" -> {
                        TopAppBar(
                            title = { Text(text = "Ana Ekran") }
                        )
                    }
                    else -> null // Diğer ekranlarda TopBar gösterilmez
                }
                */
            },
            bottomBar = {
                if (currentRoute in listOf("searchScreen", "mainScreen")) {
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
            }
        }
    }
}