package com.example.data.api

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class GenerateContentRequest(
    @Json(name = "contents") val contents: List<Content>,
    @Json(name = "systemInstruction") val systemInstruction: Content? = null
)

data class Content(
    @Json(name = "parts") val parts: List<Part>
)

data class Part(
    @Json(name = "text") val text: String? = null
)

data class GenerateContentResponse(
    @Json(name = "candidates") val candidates: List<Candidate>? = null
)

data class Candidate(
    @Json(name = "content") val content: Content? = null
)

interface GeminiRetrofitService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val service: GeminiRetrofitService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiRetrofitService::class.java)
    }

    private val kidSystemInstruction = Content(
        parts = listOf(
            Part(
                text = "You are Inno 🤖, a cheerful, patient, 3D white and cyan robot assistant teaching children aged 6-14 about Artificial Intelligence! Always explain concepts in simple, playful, encouraging words with cute emojis. Keep answers brief, safe, fun, and easy for kids to understand. Never share inappropriate or dangerous material."
            )
        )
    )

    suspend fun askInno(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "GEMINI_API_KEY") {
            return@withContext generateLocalInnoResponse(prompt)
        }

        try {
            val request = GenerateContentRequest(
                contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                systemInstruction = kidSystemInstruction
            )
            val response = service.generateContent(apiKey, request)
            val resultText = response.candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text

            if (!resultText.isNullOrEmpty()) {
                resultText
            } else {
                generateLocalInnoResponse(prompt)
            }
        } catch (e: Exception) {
            generateLocalInnoResponse(prompt)
        }
    }

    private fun generateLocalInnoResponse(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("hello") || lower.contains("hi") -> 
                "Beep boop! Hi there, young inventor! 🤖 I'm Inno, your AI buddy. What amazing secret about computers and AI shall we explore today? ✨"
            lower.contains("what is ai") -> 
                "AI stands for Artificial Intelligence! 🧠 It's like teaching computer brains how to spot patterns, recognize puppy photos, or play games just like humans do!"
            lower.contains("fact") -> 
                "🤖 Inno's Fun Fact: Did you know computers process millions of math puzzles every single second? But they still need smart kids like you to tell them what to build!"
            lower.contains("challenge") -> 
                "🚀 Inno's Challenge: Can you tell me if your smart TV or home speaker uses AI? Try spotting 3 AI gadgets in your home today!"
            lower.contains("story") -> 
                "📖 Once upon a time, a friendly little robot named Inno learned how to recognize colorful stars in space! With every star Inno found, a new constellation lit up in the sky!"
            lower.contains("prompt") -> 
                "✨ Prompt Secret: A prompt is like a magic spell you type to an AI! The more specific you are (e.g., 'Draw a cute cat with purple astronaut wings'), the cooler the AI answer will be!"
            else -> 
                "Beep Boop! That's super cool! 🌟 AI helps us solve big problems, create art, and build futuristic gadgets! Keep asking questions, future genius! 🚀"
        }
    }
}
