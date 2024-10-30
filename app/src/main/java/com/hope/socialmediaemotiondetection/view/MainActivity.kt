package com.hope.socialmediaemotiondetection.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hope.socialmediaemotiondetection.view.ui.theme.SocialMediaEmotionDetectionTheme
import com.hope.socialmediaemotiondetection.view.Login.InfoScreen
import com.hope.socialmediaemotiondetection.view.Login.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocialMediaEmotionDetectionTheme {
                LoginScreen()
            }
        }
    }
}

