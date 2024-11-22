package com.hope.socialmediaemotiondetection.model.post.comment

/*
Postların Comment koleksiyonunu rahatça alabilmek için data modelim idler benzersiz keylerimi oluşturuken
content yorum içeriği createdAt oluşturma tarihi Any olmasının sebebi firebasenin timestamp olarak
November 20, 2024 at 1:35:42 AM UTC+3 şeklinde ne geliceği belli değil o yüzden Any olarak geliyor
 */

data class Comment(
    val commentId: String = "",
    val userId: String= "",
    val content: String= "",
    val createdAt: Any,
    val emotion: String= ""
)
