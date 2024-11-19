package com.hope.socialmediaemotiondetection.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hope.socialmediaemotiondetection.model.post.Post
import kotlinx.coroutines.launch

@Composable
fun PostItem(
    username: String,
    post: Post,
    initialLiked: Boolean,
    onLikeClicked: (String) -> Unit,
    onUnlikeClicked: (String) -> Unit,
) {
    var likeCount by remember { mutableIntStateOf(post.likesCount) }
    var isLiked by remember { mutableStateOf(initialLiked) }
    var isProcessing by remember { mutableStateOf(false) }
    var isAnimating by remember { mutableStateOf(false) }

    LaunchedEffect(initialLiked) {
        isLiked = initialLiked
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "User Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = username,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = post.content,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val likeIcon = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                val likeColor = if (isLiked) Color.Red else Color.Gray

                val scale by animateFloatAsState(
                    targetValue = if (isAnimating) 1.2f else 1f,
                    animationSpec = tween(durationMillis = 300),
                    label = "Like Button Animation"
                )

                Icon(
                    imageVector = likeIcon,
                    contentDescription = "Like Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .graphicsLayer(scaleX = scale, scaleY = scale)
                        .clickable(enabled = !isProcessing) {
                            if (!isProcessing) {
                                isProcessing = true
                                isAnimating = true

                                if (isLiked) {
                                    onUnlikeClicked(post.postId)
                                    likeCount -= 1
                                } else {
                                    onLikeClicked(post.postId)
                                    likeCount += 1
                                }
                                isLiked = !isLiked

                                kotlinx.coroutines.MainScope().launch {
                                    kotlinx.coroutines.delay(500L)
                                    isAnimating = false
                                    isProcessing = false
                                }
                            }
                        },
                    tint = likeColor
                )
                Text(text = likeCount.toString())
            }
            Spacer(modifier = Modifier.width(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Comment Icon",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* Yorum işlemi */ }
                )
                Text(text = post.commentsCount.toString())
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 7.dp))
    }
}



