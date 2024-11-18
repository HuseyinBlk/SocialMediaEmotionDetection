package com.hope.socialmediaemotiondetection.view.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hope.socialmediaemotiondetection.R
import com.hope.socialmediaemotiondetection.model.post.Post
import com.hope.socialmediaemotiondetection.model.user.User
import com.hope.socialmediaemotiondetection.view.components.PostItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(user: User) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "User Profile") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavigationBar(modifier = Modifier.height(80.dp), containerColor = MaterialTheme.colorScheme.primary) {
                NavigationBarItem(
                    onClick = { /* Home tıklama işlemi */ },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home Icon", tint = Color.White) }
                )
                NavigationBarItem(
                    onClick = { /* Search tıklama işlemi */ },
                    selected = false,
                    icon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon", tint = Color.White) }
                )
                NavigationBarItem(
                    onClick = { /* Profile tıklama işlemi */ },
                    selected = true,
                    icon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Profile Icon", tint = Color.White) }
                )
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Profil başlığı ve fotoğraf
                ProfileHeader(user = user)

                // Kullanıcının paylaşımlarını listele
                LazyColumn(modifier = Modifier.fillMaxSize()) {

                }
            }
        }
    )
}

@Composable
fun ProfileHeader(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = user.username, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Followers: 30  |  Following: 30")
    }
}
