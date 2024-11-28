package com.hope.socialmediaemotiondetection.view.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hope.socialmediaemotiondetection.model.post.CommentWithUsername
import com.hope.socialmediaemotiondetection.model.post.Post
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
    val postCommentResult by viewModel.getAllCommentUserName.collectAsState()
    val postAddCommentResult by viewModel.postAddCommentResult.collectAsState()
    var allCommentUser by remember { mutableStateOf<List<CommentWithUsername>>(listOf()) }
    var likeCount by remember { mutableIntStateOf(post.likesCount) }
    var isLiked by remember { mutableStateOf(initialLiked) }
    var isProcessing by remember { mutableStateOf(false) }
    var isAnimating by remember { mutableStateOf(false) }
    var isBottomSheetVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(initialLiked) {
        isLiked = initialLiked
    }

    LaunchedEffect(postAddCommentResult) {
        viewModel.fetchCommentsWithUsernames(postId = post.postId)
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
                    color = MaterialTheme.colorScheme.onBackground,
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

                                coroutineScope.launch {
                                    delay(500L)
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
                        .clickable {
                            viewModel.fetchCommentsWithUsernames(postId = post.postId)
                            isBottomSheetVisible = true
                        }
                )
                Text(text = post.commentsCount.toString())
            }
        }
        HorizontalDivider(modifier = Modifier.padding(top = 7.dp))
    }

    if (isBottomSheetVisible) {
        when (postCommentResult) {
            is Resource.Failure -> {
                // Hata durumunda yapılacak işlem
                // Örneğin: hata mesajı gösterilebilir.
            }
            is Resource.Idle -> {
                // Idle durumunda yapılacak işlem
                // Örneğin: yükleme işlemi başlayabilir.
            }
            is Resource.Loading -> {
                // Loading durumunda yapılacak işlem
                // Örneğin: bir yükleniyor göstergesi gösterilebilir.
            }
            is Resource.Success -> {
                val commentsWithUsernames = (postCommentResult as Resource.Success<List<CommentWithUsername>>).data  // Burada data'ya erişiyorsunuz.
                allCommentUser = commentsWithUsernames
                ModalBottomSheet(
                    onDismissRequest = { isBottomSheetVisible = false }
                ) {
                    CommentSection(
                        comments = allCommentUser, // Örnek veri
                        onCommentAdded = { comment ->
                            onCommentAdded(comment)  // Yorum ekleme işlemi
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun CommentSection(
    comments: List<CommentWithUsername>,
    onCommentAdded: (comment: String) -> Unit
) {
    var newComment by remember { mutableStateOf("") }
    var lastCommentTime by remember { mutableLongStateOf(0L) }


    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newComment,
                onValueChange = {
                    if (it.length <= 256) {
                    newComment = it
                    }
                },
                placeholder = { Text("Yorum yaz...") },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val currentTime = System.currentTimeMillis()
                    if (newComment.isNotBlank() && currentTime - lastCommentTime > 5000 && newComment.length in 5..256) {
                        onCommentAdded(newComment)
                        newComment = ""
                        lastCommentTime = currentTime
                    } else {
                        println("Beklenmedik hata")
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(40.dp)
                    .padding(horizontal = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Gönder",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Gönder",
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(comments) { comment ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .animateContentSize(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = comment.comment.content.first().uppercaseChar().toString(),
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            val modifiedUsername = comment.username.drop(8).dropLast(1)
                            Text(
                                text = modifiedUsername,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = comment.comment.content,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}


