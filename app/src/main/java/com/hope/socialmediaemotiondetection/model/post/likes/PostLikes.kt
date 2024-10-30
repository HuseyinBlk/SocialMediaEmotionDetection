package com.hope.socialmediaemotiondetection.model.post.likes

data class PostLikes(
    val postId: String,
    val likes: MutableList<Like> = mutableListOf()
)
