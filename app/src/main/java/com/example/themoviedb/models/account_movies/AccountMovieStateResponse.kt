package com.example.themoviedb.models.account_movies

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class AccountMovieStateResponse(
    val favorite: Boolean,
    val id: Int,
    @SerializedName("rated")
    val movieRating: MovieRating,
    val watchlist: Boolean
)
