package com.hope.socialmediaemotiondetection.view.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hope.socialmediaemotiondetection.R
import com.hope.socialmediaemotiondetection.view.components.ActionButton
import com.hope.socialmediaemotiondetection.view.ui.theme.PrimaryVioletDark
import com.hope.socialmediaemotiondetection.view.ui.theme.PrimaryYellowDark

@Preview
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Image(painter = painterResource(R.drawable.deeplearnin1),
            contentDescription = null, //görme engelli kullanıcıların içeriği anlaması için metin
            modifier = Modifier.size(500.dp),


        )
        Text(text = "Welcome to our social media Emotion Detection App!")
        Text(text = "Login or create Account.")
        Spacer(modifier = Modifier.weight(weight = 1f))
        ActionButton(
            text ="Action Text" ,
            isNavigationArrowVisible = true,
            onClicked = { },
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = PrimaryVioletDark
            ),
            shadowColor = PrimaryVioletDark
        )
        Spacer(modifier = Modifier.weight(weight = 0.1f))
    }

}