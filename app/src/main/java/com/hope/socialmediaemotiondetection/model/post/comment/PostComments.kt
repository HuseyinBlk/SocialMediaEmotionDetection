package com.hope.socialmediaemotiondetection.model.post.comment

/*
Bu dosya çok işime yaramadı açıkcası biraz yanlış düşünüldü ama kullanılan yerler var posta ait
commenteleri toplu çekmeme yarıyor
 */
data class PostComments(
    val postId: String = "",
    val comments: List<Comment> = emptyList()
)