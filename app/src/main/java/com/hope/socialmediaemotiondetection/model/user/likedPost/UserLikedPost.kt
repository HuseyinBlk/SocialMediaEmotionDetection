package com.hope.socialmediaemotiondetection.model.user.likedPost

data class UserLikedPost(
    val userId: String,
    val likedPosts: MutableList<LikedPost> = mutableListOf()
)