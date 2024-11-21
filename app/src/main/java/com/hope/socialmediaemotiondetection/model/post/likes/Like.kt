package com.hope.socialmediaemotiondetection.model.post.likes

/*
Like zamanı ve userId çekiyor
 */

data class Like(
    val userId: String,
    val likedAt: Any
)
