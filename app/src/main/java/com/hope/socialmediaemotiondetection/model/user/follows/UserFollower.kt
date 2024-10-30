package com.hope.socialmediaemotiondetection.model.user.follows

data class UserFollower(
    val userId: String,
    val follower: MutableMap<String, Follower> = mutableMapOf()
)
