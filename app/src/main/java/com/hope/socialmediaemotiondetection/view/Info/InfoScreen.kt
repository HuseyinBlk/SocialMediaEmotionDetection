package com.hope.socialmediaemotiondetection.view.Info

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hope.socialmediaemotiondetection.R
import com.hope.socialmediaemotiondetection.view.components.ActionButton
import com.hope.socialmediaemotiondetection.view.ui.theme.getGradientBrush

@Composable
fun InfoScreen(
    navController: NavController
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(brush = getGradientBrush(isDarkTheme = isSystemInDarkTheme())),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Image(painter = painterResource(R.drawable.deeplearning),
            contentDescription = null,
            modifier = Modifier
                .size(400.dp)
                .padding(top = 32.dp)
                .alpha(0.6f)
                .padding(horizontal = 20.dp),
            colorFilter = ColorFilter.tint(
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier
            .height(16.dp))
        Text(text = "Welcome To Our \n Social Media Emotion Detection App!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(horizontal = 12.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier
            .height(16.dp))
        Text(text = "Click Next Button For \n Login or Create Account.",
            textAlign = TextAlign.Center,
            fontSize =21.sp,
            modifier = Modifier.padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(150.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Butonlar arasındaki boşluk
            verticalAlignment = Alignment.CenterVertically
        ){
            ActionButton(
                text ="Log In" ,
                isNavigationArrowVisible = true,
                onClicked = {navController.navigate("loginScreen")},
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .weight(1f)
                    .alpha(0.9f)
                    .padding(8.dp)

            )
            ActionButton(
                text ="Sign In" ,
                isNavigationArrowVisible = true,
                onClicked = {
                    navController.navigate("registerScreen") },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .weight(1f)
                    .alpha(0.9f)
                    .padding(8.dp)

            )
        }

    }

}