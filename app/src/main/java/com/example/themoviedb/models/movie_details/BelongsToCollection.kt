package com.example.themoviedb.models.movie_details

import com.google.gson.annotations.SerializedName

data class BelongsToCollection(
    val id: Int,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val name: String,
    @SerializedName("poster_path")
    val posterPath: String?
)
