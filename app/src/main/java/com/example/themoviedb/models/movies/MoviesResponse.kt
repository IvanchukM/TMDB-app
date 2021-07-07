package com.example.themoviedb.models.movies

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    val page: Int,
    @SerializedName("results")
    val movieModel: List<MoviesModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
