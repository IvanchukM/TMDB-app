package com.example.themoviedb.utils

import com.example.themoviedb.models.MovieCommonDataModel

interface OnMovieClickListener {
    fun onMovieClick(
        movieCommonDataModel: MovieCommonDataModel
    )
}