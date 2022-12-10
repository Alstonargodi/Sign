package com.example.bisindo_recognitions.model.remote

import com.example.bisindo_recognitions.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BisindoApiConfig {
    private val baseUrl = "https://msyrtrgxtbewguecsyld.supabase.co/rest/v1/"

    fun setBisindoApiService(): BisindoApiService {
        val loggingInterceptor =
            if(BuildConfig.DEBUG){
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            }else{
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(BisindoApiService::class.java)
    }
}