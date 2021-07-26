package com.example.themoviedb.models.favorite_movies

import com.google.gson.annotations.SerializedName

data class FavoriteMoviesResponse(
    val page: Int,
    @SerializedName("results")
    val favoriteMovieModels: List<FavoriteMovieModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
