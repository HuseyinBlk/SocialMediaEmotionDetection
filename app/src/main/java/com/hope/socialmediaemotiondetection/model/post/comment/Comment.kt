package com.hope.socialmediaemotiondetection.model.post.comment

data class Comment(
    val commentId: String,
    val userId: String,
    val content: String,
    val createdAt: String,
    val emotion: String
)
