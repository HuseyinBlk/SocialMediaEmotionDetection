package com.hope.socialmediaemotiondetection.model.post.comment

data class Comment(
    val commentId: String = "",
    val userId: String= "",
    val content: String= "",
    val createdAt: Any= "",
    val emotion: String= ""
)
