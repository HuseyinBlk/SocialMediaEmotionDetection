package com.hope.socialmediaemotiondetection.model.post.likes

/*
PostLikes bir posta ait tüm likeleri getiri tamamen ihtiyac dahilinde olucak birşey
 */
data class PostLikes(
    val postId: String,
    val likes: MutableList<Like> = mutableListOf()
)
