package com.hope.socialmediaemotiondetection.service

import com.hope.socialmediaemotiondetection.model.emotion.EmotionRequest
import com.hope.socialmediaemotiondetection.model.emotion.EmotionResponse
import com.hope.socialmediaemotiondetection.utils.Constants.PREDICT_ENDPOINT
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/*
Predict işlemini soyutluyoruz
Bu şekilde belirli API endpointlerine yapılan HTTP isteklerini tanımlayıp çağırabiliriz yani kotlinde
bu retrofitte zorunlu
 */

interface EmotionApiService {
    @POST(PREDICT_ENDPOINT)
    suspend fun analyzeEmotion(
        @Body request: EmotionRequest
    ): Response<EmotionResponse>
}