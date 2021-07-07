package com.example.themoviedb.models.movie_reviews

import com.google.gson.annotations.SerializedName

data class AuthorDetails(
    @SerializedName("avatar_path")
    val avatarString: String?,
    val name: String?,
    val rating: Float?,
    val username: String?
)
