package com.hope.socialmediaemotiondetection.model.post

import com.hope.socialmediaemotiondetection.model.post.comment.Comment

data class CommentWithUsername(
    val comment: Comment ,
    val username: String = ""
)