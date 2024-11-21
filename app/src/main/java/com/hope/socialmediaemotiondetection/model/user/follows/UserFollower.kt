package com.hope.socialmediaemotiondetection.model.user.follows
/*
Kullanıcının tüm takipçileri
 */
data class UserFollower(
    val userId: String,
    val follower: MutableMap<String, Follower> = mutableMapOf()
)
