package com.example.themoviedb.models.account_movies

import com.google.gson.annotations.SerializedName

data class MovieRating(
    @SerializedName("rated")
    val isMovieRated: Boolean?,
    @SerializedName("value")
    val rating: Float?
)