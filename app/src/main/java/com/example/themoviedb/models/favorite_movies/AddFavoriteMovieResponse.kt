package com.example.themoviedb.models.favorite_movies

import com.google.gson.annotations.SerializedName

data class AddFavoriteMovieResponse(
    @SerializedName("status_code")
    val statusCode: Int,
    @SerializedName("status_message")
    val statusMessage: String,
    val success: Boolean
)