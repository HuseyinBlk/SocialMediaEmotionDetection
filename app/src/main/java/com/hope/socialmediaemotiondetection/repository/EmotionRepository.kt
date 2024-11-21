package com.hope.socialmediaemotiondetection.repository

import com.hope.socialmediaemotiondetection.model.emotion.EmotionRequest
import com.hope.socialmediaemotiondetection.model.emotion.EmotionResponse
import com.hope.socialmediaemotiondetection.model.result.Resource
import com.hope.socialmediaemotiondetection.service.EmotionApiService
import javax.inject.Inject

class EmotionRepository @Inject constructor(
    private val apiService: EmotionApiService
) {
    suspend fun analyzeEmotion(input: String): Resource<EmotionResponse> {
        return try {
            val request = EmotionRequest(input = input)
            val response = apiService.analyzeEmotion(request)

            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Failure("Yanıt boş!")
            } else {
                Resource.Failure("Hata kodu: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            Resource.Failure("Bir hata oluştu: ${e.message}")
        }
    }
}