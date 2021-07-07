package com.example.themoviedb.models.movie_details

import com.google.gson.annotations.SerializedName

data class MovieActors(
    val adult: Boolean,
    @SerializedName("cast_id")
    val castId: Int,
    val character: String,
    @SerializedName("credit_id")
    val creditId: String,
    val gender: Int,
    val id: Int,
    @SerializedName("known_for_department")
    val department: String,
    val name: String,
    val order: Int,
    @SerializedName("original_name")
    val originalName: String,
    val popularity: Double,
    @SerializedName("profile_path")
    val profilePath: String?
)
