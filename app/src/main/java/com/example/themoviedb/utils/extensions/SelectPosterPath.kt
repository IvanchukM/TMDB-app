package com.example.themoviedb.utils.extensions

import com.example.themoviedb.models.movies.MoviesModel

fun MoviesModel.selectPosterPath(): String? {
    return when {
        this.backdropPath != null -> this.backdropPath
        this.posterPath != null -> this.posterPath
        else -> null
    }
}
