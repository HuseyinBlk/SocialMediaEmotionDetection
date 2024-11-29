package com.hope.socialmediaemotiondetection.view.Profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hope.socialmediaemotiondetection.R
import com.hope.socialmediaemotiondetection.model.post.Post
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.model.user.User
import com.hope.socialmediaemotiondetection.model.user.comment.Comment
import com.hope.socialmediaemotiondetection.model.user.dailyEmotion.DailyEmotion
import com.hope.socialmediaemotiondetection.view.components.SingleChoiceSegmentedButton
import com.hope.socialmediaemotiondetection.view.ui.theme.SocialMediaEmotionDetectionTheme
import com.hope.socialmediaemotiondetection.viewmodel.ProfileDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UserProfileScreen(viewModel: ProfileDetailsViewModel = hiltViewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val postsResult by viewModel.postsResult.collectAsState()
    val postsRemoveResult by viewModel.postsRemoveResult.collectAsState()
    val userRemoveCommentResult by viewModel.removeCommentResult.collectAsState()
    val postUsername by viewModel.usernameResult.collectAsState()
    val postFollowing by viewModel.followingListLengthResult.collectAsState()
    val postFollowers by viewModel.followersListLengthResult.collectAsState()
    val userCommentsResult by viewModel.userCommentsResult.collectAsState()
    val userDailyEmotion by viewModel.dailyEmotionState.collectAsState()
    LaunchedEffect(postsRemoveResult) {
       viewModel.getPostsByUser()
    }
    LaunchedEffect(userRemoveCommentResult) {
        viewModel.getUserComments()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
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
                    text = when (postUsername) {
                        is Resource.Loading -> "Yükleniyor..."
                        is Resource.Success -> (postUsername as Resource.Success<String?>).data ?: "Kullanıcı adı mevcut değil"
                        is Resource.Failure -> "Hata: ${(postUsername as Resource.Failure).message}"
                        else -> "Kullanıcı adı bulunamadı"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }

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
                            text = when (postFollowing) {
                                is Resource.Loading -> "Yükleniyor..."
                                is Resource.Success -> ((postFollowing as Resource.Success<String>).data ?: 0).toString()
                                is Resource.Failure -> "Hata: ${(postFollowing as Resource.Failure<String>).message}"
                                else -> "Kullanıcı adı bulunamadı"
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 8.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Takip",
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(60.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when (postFollowers) {
                                is Resource.Loading -> "Yükleniyor..."
                                is Resource.Success -> ((postFollowers as Resource.Success<String>).data ?: 0).toString()
                                is Resource.Failure -> "Hata: ${(postFollowers as Resource.Failure<String>).message}"
                                else -> "Kullanıcı adı bulunamadı"
                            },
                            fontSize = 20.sp,
                            modifier = Modifier.padding(bottom = 8.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Takipçi",
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Text(
                text = "",
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
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            SingleChoiceSegmentedButton(onSelectionChanged = { selectedTab = it })
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            when (selectedTab) {
                0 -> {
                    when (postsResult) {
                        is Resource.Loading -> {
                            item {
                                Text("Gönderiler yükleniyor...", modifier = Modifier.padding(16.dp))
                            }
                        }

                        is Resource.Success -> {
                            if ((postsResult as? Resource.Success<List<Post>>)?.data.isNullOrEmpty()) {
                                item {
                                    Text("Gösterilecek gönderi yok", modifier = Modifier.padding(16.dp))
                                }
                            }else{
                                items((postsResult as Resource.Success<List<Post>>).data) { post ->
                                    Card(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        shape = MaterialTheme.shapes.medium,
                                        elevation = CardDefaults.cardElevation(4.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(16.dp)
                                        ) {
                                            Text(
                                                text = post.content,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            )

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Duygu Durumu
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Duygu: ",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                                Badge(
                                                    containerColor = when (post.emotion) {
                                                        "happy" -> Color(0xFF81C784) // Yeşil
                                                        "sad" -> Color(0xFFE57373) // Kırmızı
                                                        "angry" -> Color(0xFFD32F2F) // Kızıl
                                                        "love" -> Color(0xFFFF4081) // Pembe
                                                        "fear" -> Color(0xFF9E9E9E) // Gri
                                                        "surprise" -> Color(0xFF64B5F6) // Mavi
                                                        else -> Color(0xFF64B5F6)  // Mavi
                                                    }
                                                ) {
                                                    Text(
                                                        text = post.emotion,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = "Beğeni: ${post.likesCount}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                                Text(
                                                    text = "Yorum: ${post.commentsCount}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                                Text(
                                                    text = "Tarih: ${viewModel.formatTimestampToDate(post.createdAt)}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )

                                                // Silme İkonu
                                                IconButton(
                                                    onClick = { viewModel.removePosts(post.postId) } // Post ID ile silme işlemi
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Gönderiyi Sil",
                                                        tint = Color.Red,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }

                        is Resource.Failure -> {
                            item{
                                Text(
                                    text = "Gönderiler yüklenemedi: ${(postsResult as Resource.Failure).message}",
                                    color = Color.Red,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        else -> {}
                    }
                }
                1 -> {
                    when (userCommentsResult) {
                        is Resource.Loading -> {

                            CoroutineScope(Dispatchers.Main).launch {
                                item {
                                    Text("Yorumlar yükleniyor...", modifier = Modifier.padding(16.dp))
                                }
                                delay(1000)
                            }
                        }

                        is Resource.Success -> {
                            val comments = (userCommentsResult as Resource.Success<Map<String, Comment>>).data
                            if (comments.isEmpty()) {
                                item {
                                    Text("Gösterilecek yorum yok", modifier = Modifier.padding(16.dp))
                                }
                            } else {
                                items(comments.values.toList()) { comment -> // Yorumları listeleyin
                                    Card(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth(),
                                        shape = MaterialTheme.shapes.medium,
                                        elevation = CardDefaults.cardElevation(4.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = comment.content ?: "",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                    modifier = Modifier.padding(bottom = 8.dp)
                                                )

                                                Spacer(modifier = Modifier.height(8.dp))

                                                Text(
                                                    text = "Yorum yapılan gönderinin duygu durumu: ${comment.emotion}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    viewModel.removeComment(commentId = comment.commentId ?: "")
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Yorumu Sil",
                                                    tint = Color.Red
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        is Resource.Failure -> {
                            item {
                                Text(
                                    text = "Yorumlar yüklenemedi: ${(userCommentsResult as Resource.Failure).message}",
                                    color = Color.Red,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        else -> {}
                    }
                }
                2 -> {
                    when (userDailyEmotion) {
                        is Resource.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                            }
                        }

                        is Resource.Success -> {
                            val emotionData = (userDailyEmotion as Resource.Success<DailyEmotion?>).data
                            item {
                                if (emotionData != null) {
                                    val emotions = emotionData.emotions
                                    val dominantEmotion = emotions.maxByOrNull { it.value }?.key // En yüksek duyguyu alıyoruz

                                    Card(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(),
                                        shape = MaterialTheme.shapes.large,
                                        elevation = CardDefaults.cardElevation(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .padding(24.dp)
                                                .fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = "Bugünkü Duygu Durumunuz",
                                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                modifier = Modifier.padding(bottom = 12.dp)
                                            )

                                            Spacer(modifier = Modifier.height(16.dp))

                                            dominantEmotion?.let {
                                                Badge(
                                                    containerColor = when (it) {
                                                        "happy" -> Color(0xFF81C784) // Yeşil
                                                        "sad" -> Color(0xFFE57373) // Kırmızı
                                                        "angry" -> Color(0xFFD32F2F) // Kızıl
                                                        "love" -> Color(0xFFFF4081) // Pembe
                                                        "fear" -> Color(0xFF9E9E9E) // Gri
                                                        "surprise" -> Color(0xFF64B5F6) // Mavi
                                                        else -> Color(0xFF64B5F6)  // Varsayılan renk
                                                    },
                                                    modifier = Modifier.padding(8.dp)
                                                ) {
                                                    Text(
                                                        text = it.capitalize(),
                                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(24.dp))

                                            emotions.forEach { (emotion, count) ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(bottom = 10.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = emotion.capitalize(),
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )

                                                    Text(
                                                        text = "$count",
                                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "Bugün için duygu durumu bulunamadı",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color.Gray,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }

                        is Resource.Failure -> {
                            item {
                                Text(
                                    text = "Duygu durumu yüklenemedi: ${(userDailyEmotion as Resource.Failure).message}",
                                    color = Color.Red,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

    }
}


