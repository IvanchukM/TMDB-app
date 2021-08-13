package com.example.themoviedb.models.account_movies

import com.google.gson.annotations.SerializedName

data class AccountMoviesResponse(
    val page: Int,
    @SerializedName("results")
    val accountMoviesModel: List<AccountMovieModel>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
