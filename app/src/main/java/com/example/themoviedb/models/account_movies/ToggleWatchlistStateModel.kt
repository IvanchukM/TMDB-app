package com.example.themoviedb.models.account_movies

import com.google.gson.annotations.SerializedName

data class ToggleWatchlistStateModel(
    @SerializedName("media_type")
    val mediaType: String = "movie",
    @SerializedName("media_id")
    val movieId: Int,
    @SerializedName("watchlist")
    val isInWatchlist: Boolean
)
