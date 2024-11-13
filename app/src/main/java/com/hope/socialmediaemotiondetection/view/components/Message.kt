package com.hope.socialmediaemotiondetection.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Message(
    modifier: Modifier=Modifier,
    title: String,
    subtitle : String,
    textColor : Color,
    fontWeight: FontWeight
){
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ){
        Text(
            text = title,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
            fontWeight = fontWeight
        )
        Text(
            text = subtitle,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            color = textColor,
            fontWeight = fontWeight

        )
    }
}