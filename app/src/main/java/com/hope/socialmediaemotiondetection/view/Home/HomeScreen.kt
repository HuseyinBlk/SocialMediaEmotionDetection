package com.hope.socialmediaemotiondetection.view.Home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items//ÇOK ÖNEMLİ
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hope.socialmediaemotiondetection.view.components.PostItem
import com.hope.socialmediaemotiondetection.view.components.samplePosts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var inputValue by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = @Composable{ Text("Home", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                actions = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Theme Toggle Icon"
                        )
                }
            )
        },
        bottomBar = {
            NavigationBar (){
                NavigationBarItem(
                    onClick = {
                        // Tıklama olayını buraya ekleyin
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home Icon"
                        )
                    }
                )
                NavigationBarItem(
                    onClick = {
                        // Tıklama olayını buraya ekleyin
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon"
                        )
                    }
                )
                NavigationBarItem(
                    onClick = {
                        // Tıklama olayını buraya ekleyin
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile Icon"
                        )
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Yeni gönderi alanı
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Icon",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                TextField(
                    value = inputValue,
                    onValueChange = {inputValue = it},
                    placeholder = { Text("Bugün Nasılsın?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                IconButton(onClick = { /* Gönderi ekleme işlemi */ }) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Icon"
                    )
                }
            }
            Divider()
            // Gönderi liste
            LazyColumn {
                items(samplePosts) { post ->
                    PostItem(post)
                }
            }
        }
    }
}

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