package com.example.themoviedb.models.account_movies

import com.google.gson.annotations.SerializedName

data class ToggleMovieRateModel(
    @SerializedName("media_type")
    val mediaType: String = "movie",
    @SerializedName("media_id")
    val movieId: Int,
    @SerializedName("favorite")
    val rate: Any
)
