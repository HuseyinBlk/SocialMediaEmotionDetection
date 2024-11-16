package com.hope.socialmediaemotiondetection.view.Home

import android.widget.Toast
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.view.components.PostItem
import com.hope.socialmediaemotiondetection.view.components.samplePosts
import com.hope.socialmediaemotiondetection.view.ui.theme.renk4
import com.hope.socialmediaemotiondetection.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var inputValue by remember { mutableStateOf("") }
    val postResult by homeViewModel.postResult.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(postResult) {
        when (postResult) {
            is Resource.Success -> {
                Toast.makeText(context, "Gönderi başarıyla eklendi!", Toast.LENGTH_SHORT).show()
                homeViewModel.resetPostResult()
            }
            is Resource.Failure -> {
                Toast.makeText(
                    context,
                    (postResult as Resource.Failure).message,
                    Toast.LENGTH_SHORT
                ).show()
                homeViewModel.resetPostResult()
            }
            else -> {}
        }
    }
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
            NavigationBar (
                modifier = Modifier.height(80.dp),
                containerColor = renk4
            ){
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
                IconButton(onClick = {
                    homeViewModel.addPost("mutlu",inputValue)
                    inputValue = ""
                }) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Icon"
                    )
                }
            }
            HorizontalDivider()
            // Gönderi liste
            LazyColumn {
                items(samplePosts) { post ->
                    PostItem(post)
                }
            }
        }
    }
}

