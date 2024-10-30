package com.hope.socialmediaemotiondetection.view.Login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hope.socialmediaemotiondetection.R
import com.hope.socialmediaemotiondetection.view.components.ActionButton
import com.hope.socialmediaemotiondetection.view.ui.theme.DarkTextColor
import com.hope.socialmediaemotiondetection.view.ui.theme.PrimaryVioletDark
import com.hope.socialmediaemotiondetection.view.ui.theme.PrimaryYellowDark
import com.hope.socialmediaemotiondetection.view.ui.theme.PrimaryYellowLight
import com.hope.socialmediaemotiondetection.view.ui.theme.renk1
import com.hope.socialmediaemotiondetection.view.ui.theme.renk2
import com.hope.socialmediaemotiondetection.view.ui.theme.renk3
import com.hope.socialmediaemotiondetection.view.ui.theme.renk4
import com.hope.socialmediaemotiondetection.view.ui.theme.renk5

@Preview
@Composable
fun InfoScreen(
    modifier: Modifier = Modifier
){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(brush = Brush.verticalGradient(
                0f to renk1,
                0.25f to renk2,
                0.5f to renk3,
                0.75f to renk4
            )),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Image(painter = painterResource(R.drawable.deeplearning),
            contentDescription = null, //görme engelli kullanıcıların içeriği anlaması için metin
            modifier = Modifier
                .size(400.dp)
                .padding(top = 32.dp)


        )
        Spacer(modifier = Modifier
            .height(16.dp))
        Text(text = "Welcome To Our \n Social Media Emotion Detection App!",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(horizontal = 12.dp),
            color = DarkTextColor
        )
        Spacer(modifier = Modifier
            .height(16.dp))
        Text(text = "Click Next Button For \n Login or Create Account.",
            textAlign = TextAlign.Center,
            fontSize =21.sp,
            modifier = Modifier.padding(horizontal = 24.dp),
            color = DarkTextColor
        )
        Spacer(modifier = Modifier.height(150.dp))
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Butonlar arasındaki boşluk
            verticalAlignment = Alignment.CenterVertically
        ){
            ActionButton(
                text ="Sign Up" ,
                isNavigationArrowVisible = true,
                onClicked = { },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = renk5
                ),
                shadowColor = PrimaryVioletDark,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)

            )
            ActionButton(
                text ="Sign In" ,
                isNavigationArrowVisible = true,
                onClicked = { },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = renk5
                ),
                shadowColor = PrimaryVioletDark,
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)

            )
        }

    }

}