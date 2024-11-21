package com.hope.socialmediaemotiondetection.model.user
/*
Kullanıcı detayları
 */

data class User(
    val userId: String = "",
    var username: String = "",
    var email: String = "",
    var profileImage: String? = null,
    var bio: String? = null,
    var interests: List<String?> = emptyList()
)