package com.hope.socialmediaemotiondetection.view.components

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hope.socialmediaemotiondetection.model.post.Post
import com.hope.socialmediaemotiondetection.view.ui.theme.SocialMediaEmotionDetectionTheme
import java.nio.file.WatchEvent

@Composable
fun PostItem(post: Post){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        Row (verticalAlignment = Alignment.CenterVertically)
        {
            Icon(imageVector = Icons.Default.Person,
                contentDescription = "User Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Kullanıcı Ad",
                    style = MaterialTheme.typography.bodySmall
                )
                /*
                Text(
                    text = "Yorum Yapmayı seviyorum",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                */
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(modifier = Modifier.fillMaxWidth(),
            text = post.content,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.End
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Like Icon",
                    modifier = Modifier.size(24.dp).clickable { /* Like işlemi */ } // İkona tıklanabilirlik ekledik
                )
                Text(
                    text = post.likesCount.toString(),
                )
            }
            Spacer(Modifier.width(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Comment Icon",
                    modifier = Modifier.size(24.dp).clickable { /* Like işlemi */ } // İkona tıklanabilirlik ekledik
                )
                Text(
                    text = post.commentsCount.toString(),
                )
            }

        }
        HorizontalDivider(modifier = Modifier.padding(top = 7.dp))
    }
}

val samplePosts = listOf(
    Post("post1","ben post atıyorum","morning","user1","joy",36,34),
    Post("post1","ben post atıyorum","morning","user1","joy",36,34),
    Post("post1","ben post atıyorum","morning","user1","joy",36,34),
    Post("post1","ben post atıyorum","morning","user1","joy",36,34),
    Post("post1","ben post atıyorum","morning","user1","joy",36,34),
    Post("post1","ben post atıyorum","morning","user1","joy",36,34),
    Post("post1","ben post atıyorum","morning","user1","joy",36,34),
    Post("post1","ben post atıyorum","morning","user1","joy",36,34),
    Post("post1","ben post atıyorum","morning","user1","joy",36,34),
    Post("post1","ben post atıyorum","morning","user1","joy",36,34),
    Post("post1","ben post atıyorum","morning","user1","joy",36,34),
    Post("post1","ben post atıyorum","morning","user1","joy",36,34),



)

@Preview
@Composable
fun PostItemReview(){
    SocialMediaEmotionDetectionTheme {
        val post = Post("post1","ben post atıyorum","morning","user1","joy",36,34)
        PostItem(post)
    }
}