package com.hope.socialmediaemotiondetection.model.user.comment


/*
Kullanıcının yorumlarını toplu çekmeme yarayan bir data objem
 */
data class UserComments(
    val userId: String,
    val comments: MutableList<Comment> = mutableListOf()
)
