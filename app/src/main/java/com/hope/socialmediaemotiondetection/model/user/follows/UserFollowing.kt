package com.hope.socialmediaemotiondetection.model.user.follows

data class UserFollowing(
    val userId: String,
    val following: MutableMap<String, Following> = mutableMapOf()
)