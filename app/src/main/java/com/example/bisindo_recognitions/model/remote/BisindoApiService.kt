package com.example.bisindo_recognitions.model.remote

import com.example.bisindo_recognitions.model.remote.recognitionresponse.BisindoApiResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface BisindoApiService {

    @GET("sign")
    suspend fun getBisindoSign(
        @Header("Apikey") apiKey : String = Constant.auth
    ): BisindoApiResponse
}