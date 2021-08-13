package com.example.themoviedb.models.auth

import com.google.gson.annotations.SerializedName

data class DeleteSessionResponse(
    @SerializedName("status_code")
    val status_code: Int,
    @SerializedName("status_message")
    val status_message: String,
    val success: Boolean
)