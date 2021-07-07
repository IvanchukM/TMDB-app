package com.example.themoviedb.models.movie_reviews

import com.google.gson.annotations.SerializedName

data class MovieReviewsResponse(
    val id: Int,
    val page: Int,
    @SerializedName("results")
    val reviewDetails: List<ReviewDetails>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
