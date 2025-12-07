package com.example.chatbot_tra.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface DeepLTranslateApi {
    @FormUrlEncoded
    @POST("translate")
    suspend fun translateText(
        @Header("Authorization") authHeader: String,
        @Field("text") text: String,
        @Field("target_lang") targetLanguage: String,
        @Field("source_lang") sourceLanguage: String = "KO" // 한국어 -> 영어 고정
    ): Response<DeepLTranslateResponse>
}

data class DeepLTranslateResponse(
    val translations: List<DeepLTranslation>
)

data class DeepLTranslation(
    val text: String
)

object RetrofitClient {
    //api 부분
    const val API_KEY = ""
    //api URL
    private const val BASE_URL = "https://api-free.deepl.com/v2/"

    private val okHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val deepLService: DeepLTranslateApi by lazy {
        retrofit.create(DeepLTranslateApi::class.java)
    }
}