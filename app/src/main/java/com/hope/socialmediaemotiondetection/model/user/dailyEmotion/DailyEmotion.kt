package com.hope.socialmediaemotiondetection.model.user.dailyEmotion

data class DailyEmotion(
    val date: String,
    val emotions: Map<String, Int>,
    val createdAt: String
)