package com.hope.socialmediaemotiondetection.model.user.dailyEmotion

/*
Günlük duygu durumu
 */
data class UserDailyEmotions(
    val userId: String = "",
    val dailyEmotions: Map<String, DailyEmotion> = emptyMap()
)
