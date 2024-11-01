package com.hope.socialmediaemotiondetection.model.user.comment

data class Comment(
    val postId: String,
    val content: String,
    val createdAt: Any,
    val emotion: String
)
