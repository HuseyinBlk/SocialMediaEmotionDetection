package com.hope.socialmediaemotiondetection.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hope.socialmediaemotiondetection.view.ui.theme.PrimaryVioletDark
import com.hope.socialmediaemotiondetection.view.ui.theme.SocialMediaEmotionDetectionTheme

@Composable
fun ActionButton(
    modifier: Modifier = Modifier,
    text:String,
    isNavigationArrowVisible: Boolean,
    onClicked : () ->Unit,
    colors: ButtonColors,
    shadowColor: Color
) {
    Button(
        modifier= modifier
            .fillMaxWidth()
            .height(62.dp)
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(percent = 50),
                spotColor = shadowColor
            ),
        onClick = onClicked,
        colors = colors,


    ) {
        Row (horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
            ){
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            if(isNavigationArrowVisible)
            Icon(imageVector = (Icons.Filled.KeyboardArrowRight),
                contentDescription = null ,
                modifier = Modifier.size(26.dp))
        }
    }
}
@Preview
@Composable
fun previewActionButton_NavigationVisible(){
    SocialMediaEmotionDetectionTheme {
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
    }
}

@Preview
@Composable
fun previewActionButton_Navigation(){
    SocialMediaEmotionDetectionTheme {
        Surface (modifier = Modifier
            .background(color = Color.White)

        ){
            Column (Modifier.padding(5.dp)){
                ActionButton(
                    text ="Action Text" ,
                    isNavigationArrowVisible = false,
                    onClicked = { },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = PrimaryVioletDark
                    ),
                    shadowColor = PrimaryVioletDark
                )
            }
        }
    }
}