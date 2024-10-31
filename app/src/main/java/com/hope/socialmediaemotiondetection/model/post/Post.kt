package com.hope.socialmediaemotiondetection.model.post

data class Post(
    val postId: String,
    val content: String,
    val createdAt: String,
    val userId: String,
    val emotion: String,
    var likesCount: Int = 0,
    var commentsCount: Int = 0
)
