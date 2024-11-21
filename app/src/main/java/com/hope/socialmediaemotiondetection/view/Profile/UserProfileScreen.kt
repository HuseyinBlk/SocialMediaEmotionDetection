package com.hope.socialmediaemotiondetection.view.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hope.socialmediaemotiondetection.R
import com.hope.socialmediaemotiondetection.model.user.User
import com.hope.socialmediaemotiondetection.view.components.SingleChoiceSegmentedButton
import com.hope.socialmediaemotiondetection.view.ui.theme.SocialMediaEmotionDetectionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(user: User) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            // Sol tarafta fotoğraf ve kullanıcı ismi
            Column(
                modifier = Modifier
                    .padding(start = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable(onClick = {})
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.username,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // Sağ tarafta takipçi bilgileri
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "30",
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 8.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Takipçi",
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(60.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "30",
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Takip",
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row (){
            Text(
                text = user.bio?.takeIf { it.isNotEmpty() } ?: "No bio available.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
                fontSize = 15.sp,
                lineHeight = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row (horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()){
            SingleChoiceSegmentedButton()
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn (

        ){

        }
    }
}



@Preview
@Composable
fun UserProfileScreen(){
    SocialMediaEmotionDetectionTheme {
        UserProfileScreen(User("31","Hüseyin","huseyinsbalik@gmail.com",null,"Kun fe yekün\nBabasının prensesi \nSevgilisinin hamı \nAnasının canı..\nSümeyye’nin kankası\nTarz değil farz",emptyList()))
    }
}