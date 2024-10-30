package com.hope.socialmediaemotiondetection.model.user.comment

data class UserComments(
    val userId: String,
    val comments: MutableList<Comment> = mutableListOf()
)
