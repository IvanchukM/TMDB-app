package com.example.themoviedb.models.movie_details

import com.google.gson.annotations.SerializedName

data class MovieCast(
    @SerializedName("cast")
    val actors: List<MovieActors>,
    val id: Int
)
