package com.hope.socialmediaemotiondetection.model.post.comment

data class PostComments(
    val postId: String = "",
    val comments: List<Comment> = emptyList()
)