package com.hope.socialmediaemotiondetection.service

import com.hope.socialmediaemotiondetection.model.emotion.EmotionRequest
import com.hope.socialmediaemotiondetection.model.emotion.EmotionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EmotionApiService {
    @POST("predict")
    suspend fun analyzeEmotion(
        @Body request: EmotionRequest
    ): Response<EmotionResponse>
}