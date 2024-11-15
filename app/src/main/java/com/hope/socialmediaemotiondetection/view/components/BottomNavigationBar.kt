package com.hope.socialmediaemotiondetection.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun BottomNavigationBar() {
    BottomAppBar(
        contentColor = Color.Black,
        containerColor = Color.White

    ) {
        Column (
        ){
            IconButton(onClick = { /* Home action */ }) {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
            }
            IconButton(onClick = { /* Profile action */ }) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Profile")
            }
        }

    }
}