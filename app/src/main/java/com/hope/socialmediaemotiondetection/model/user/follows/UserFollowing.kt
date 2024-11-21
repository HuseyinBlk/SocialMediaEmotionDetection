package com.hope.socialmediaemotiondetection.model.user.follows
/*
Kullanıcının takip ettikleri
 */
data class UserFollowing(
    val userId: String,
    val following: MutableMap<String, Following>? = null
)