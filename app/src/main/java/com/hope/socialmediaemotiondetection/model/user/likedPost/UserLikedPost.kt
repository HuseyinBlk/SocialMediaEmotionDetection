package com.hope.socialmediaemotiondetection.model.user.likedPost
/*
Tüm beğendiği postlar
 */
data class UserLikedPost(
    val userId: String,
    val likedPosts: MutableList<LikedPost> = mutableListOf()
)