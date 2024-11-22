package com.hope.socialmediaemotiondetection.model.user.comment

/*
Kullanıcının yaptığı yorumların detayları
 */

data class Comment(
    val commentId : String,
    val postId: String,
    val content: String,
    val createdAt: Any,
    val emotion: String
)
