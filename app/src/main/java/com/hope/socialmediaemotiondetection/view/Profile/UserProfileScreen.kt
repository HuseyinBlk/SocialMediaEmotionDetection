package com.hope.socialmediaemotiondetection.view.Profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hope.socialmediaemotiondetection.model.post.Post
import com.hope.socialmediaemotiondetection.model.user.User
import com.hope.socialmediaemotiondetection.view.components.SingleChoiceSegmentedButton
import com.hope.socialmediaemotiondetection.view.ui.theme.SocialMediaEmotionDetectionTheme
import com.hope.socialmediaemotiondetection.viewmodel.HomeViewModel

@Composable
fun UserProfileScreen(
    username: String,
    allPosts: List<Post>, // Statik post listesi
    likedPosts: List<String>, // Statik beğenilen post ID'leri
    commentedPosts: List<String>, // Statik yorum yapılan post ID'leri
) {
    var selectedTab by remember { mutableStateOf(0) } // Seçili sekme (Paylaşımlar, Beğeniler, Yorumlar)

    // Tab'a göre filtrelenmiş postları al
    val filteredPosts = when (selectedTab) {
        0 -> allPosts.filter {  } // Kullanıcının paylaştığı postlar
        1 -> allPosts.filter {  } // Kullanıcının beğendiği postlar
        2 -> allPosts.filter {  } // Kullanıcının yorum yaptığı postlar
        else -> emptyList()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Tab seçimi için segment buton
        SingleChoiceSegmentedButton(
            checkedItem = selectedTab,
            onCheckedChange = { selectedTab = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filtrelenmiş postları gösteren liste
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(filteredPosts) { post ->
                PostItem(
                    username = post.username,
                    post = post,
                    initialLiked = likedPosts.contains(post.postId),
                    onLikeClicked = { /* Firebase bağlantısını buraya ekle */ },
                    onUnlikeClicked = { /* Firebase bağlantısını buraya ekle */ },
                    onCommentAdded = { comment ->
                        /* Firebase bağlantısını buraya ekle */
                    },
                    viewModel = HomeViewModel() // Statik durumda boş bırakılmıştır
                )
            }
        }
    }
}

@Composable
fun SingleChoiceSegmentedButton(
    checkedItem: Int,
    onCheckedChange: (Int) -> Unit
) {
    val options = listOf("Paylaşımlar", "Beğeniler", "Yorumlar")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = index == checkedItem
            Button(
                onClick = { onCheckedChange(index) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            ) {
                Text(text = option)
            }
        }
    }
}

@Composable
fun PostItem(
    username: String,
    post: Post,
    initialLiked: Boolean,
    onLikeClicked: (String) -> Unit,
    onUnlikeClicked: (String) -> Unit,
    onCommentAdded: (comment: String) -> Unit,
    viewModel: HomeViewModel
) {
    var likeCount by remember { mutableIntStateOf(post.likesCount) }
    var isLiked by remember { mutableStateOf(initialLiked) }
    var isAnimating by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = username,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = post.content,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val likeIcon = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                val likeColor = if (isLiked) Color.Red else Color.Gray

                Icon(
                    imageVector = likeIcon,
                    contentDescription = "Like Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            isAnimating = true
                            if (isLiked) {
                                onUnlikeClicked(post.postId)
                                likeCount -= 1
                            } else {
                                onLikeClicked(post.postId)
                                likeCount += 1
                            }
                            isLiked = !isLiked
                        },
                    tint = likeColor
                )
                Text(text = likeCount.toString())
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Comment Icon",
                    modifier = Modifier.size(24.dp)
                )
                Text(text = post.commentsCount.toString())
            }
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