package com.example.bisindo_recognitions.model.recognitionresponse

import com.google.gson.annotations.SerializedName

class BisindoApiResponse : ArrayList<RecognitionResponseItem>()

data class RecognitionResponseItem(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String
)