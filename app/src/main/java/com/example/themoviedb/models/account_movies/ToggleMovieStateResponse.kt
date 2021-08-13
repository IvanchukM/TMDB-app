package com.example.themoviedb.models.account_movies

import com.google.gson.annotations.SerializedName

data class ToggleMovieStateResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String,
    val success: Boolean
)