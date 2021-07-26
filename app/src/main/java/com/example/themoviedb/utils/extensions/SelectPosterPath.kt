package com.example.themoviedb.utils.extensions

import com.example.themoviedb.models.favorite_movies.FavoriteMovieModel
import com.example.themoviedb.models.movies.MoviesModel

fun MoviesModel.selectPosterPath(): String? {
    return when {
        this.backdropPath != null -> this.backdropPath
        this.posterPath != null -> this.posterPath
        else -> null
    }
}
fun FavoriteMovieModel.selectPosterPath(): String? {
    return when {
        this.backdropPath != null -> this.backdropPath
        this.posterPath != null -> this.posterPath
        else -> null
    }
}
