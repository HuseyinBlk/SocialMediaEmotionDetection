package com.hope.socialmediaemotiondetection.model.user


data class User(
    val userId: String,
    var username: String,
    var email: String,
    var profileImage: String?,
    var bio: String?,
    var interests: List<String?>
)