package com.hope.socialmediaemotiondetection.model.user.comment

data class Comment(
    val postId: String,
    val content: String,
    val createdAt: String,
    val emotion: String
)
