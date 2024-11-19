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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hope.socialmediaemotiondetection.model.post.Post
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.view.components.PostItem
import com.hope.socialmediaemotiondetection.viewmodel.HomeViewModel


@Composable
fun MainScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var inputValue by remember { mutableStateOf("") }
    val postResult by homeViewModel.postResult.collectAsState()
    val getAllPost by homeViewModel.getAllPost.collectAsState()
    val postLikeResults by homeViewModel.postLikeResults.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        homeViewModel.getFollowingAllPost()
    }

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


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Spacer(modifier = Modifier.height(25.dp))
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
                onValueChange = { inputValue = it },
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
                homeViewModel.addPost("happy", inputValue)
                inputValue = ""
            }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send Icon")
            }
        }

        HorizontalDivider()

        LazyColumn {
            when (getAllPost) {
                is Resource.Success -> {
                    val postsMap = (getAllPost as Resource.Success<Map<String, List<Post>>>).data
                    postsMap.forEach { (username, posts) ->
                        items(posts) { post ->
                            homeViewModel.checkPostLikeStatus(post.postId)
                            val isLiked = when (val result = postLikeResults[post.postId]) {
                                is Resource.Success -> result.data
                                is Resource.Failure -> false
                                else -> false
                            }

                            PostItem(
                                username = username,
                                post = post,
                                isLiked = isLiked,
                                onLikeClicked = { postId -> homeViewModel.addLikedPost(postId, emotion = post.emotion) },
                                onUnlikeClicked = { postId -> homeViewModel.removeLikedPost(postId, emotion = post.emotion) }
                            )
                        }
                    }
                }
                is Resource.Failure -> {
                    item {
                        Text(
                            text = (getAllPost as Resource.Failure).message,
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is Resource.Loading -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(48.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 4.dp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Gönderiler yükleniyor...",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                else -> {
                    item {
                        Text(
                            text = "Henüz gönderi yok.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}