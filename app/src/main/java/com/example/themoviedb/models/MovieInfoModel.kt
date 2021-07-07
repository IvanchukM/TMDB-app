package com.example.themoviedb.models

import com.example.themoviedb.models.movie_details.MovieCast
import com.example.themoviedb.models.movie_details.MovieDetailsResponse
import com.example.themoviedb.models.movie_reviews.MovieReviewsResponse

data class MovieInfoModel(
    val movieInfoResponse: MovieDetailsResponse,
    val movieReviewsResponse: MovieReviewsResponse,
    val movieCast: MovieCast
)
