package com.hope.socialmediaemotiondetection.view.loading

import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hope.socialmediaemotiondetection.R
import com.hope.socialmediaemotiondetection.repository.UserNameCheckRepository
import com.hope.socialmediaemotiondetection.utils.CheckUserName

@Composable
fun CheckUserNameScreen(navController : NavController) {
    val isLoading = remember { mutableStateOf(true) }
    val firebaseAuth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    LaunchedEffect(Unit) {
        if (firebaseAuth.currentUser == null){
            navController.navigate("infoScreen"){
                popUpTo("checkUserNameScreen") { inclusive = true }
            }
        }else{
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

    }

    if (isLoading.value) {
        val infiniteTransition = rememberInfiniteTransition(label = "Loading Remember")
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = EaseOutExpo)
            ), label = "Loading Animation"
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Loading",
                modifier = Modifier
                    .size(64.dp)
                    .rotate(rotation), // Rotasyon animasyonu
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}