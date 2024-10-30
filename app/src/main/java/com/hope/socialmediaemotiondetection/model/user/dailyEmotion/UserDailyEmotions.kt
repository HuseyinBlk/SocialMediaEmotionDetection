package com.hope.socialmediaemotiondetection.model.user.dailyEmotion

data class UserDailyEmotions(
    val userId: String,
    val dailyEmotions: MutableMap<String, DailyEmotion> = mutableMapOf()
)
