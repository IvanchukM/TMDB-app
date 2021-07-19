package com.example.themoviedb.models

import com.example.themoviedb.models.movie_details.Genre
import com.example.themoviedb.models.movie_details.MovieCast
import com.example.themoviedb.models.movie_reviews.ReviewDetails
import com.example.themoviedb.utils.extensions.convertIntoData
import com.example.themoviedb.utils.extensions.selectPosterPath

data class MovieDetailsModel(
    val movieId: Int,
    val title: String,
    val posterPath: String?,
    val rating: Float?,
    val votes: Float?,
    val releaseDate: String?,
    val runtime: Int?,
    val genres: List<Genre>?,
    val actors: MovieCast?,
    val storyline: String?,
    val tagLine: String?,
    val budget: Int?,
    val revenue: Int?,
    val homePage: String?,
    val reviews: List<ReviewDetails>?
)

fun MovieInfoModel.toMovieDetailsModel() = MovieDetailsModel(
    movieId = movieInfoResponse.id,
    title = movieInfoResponse.title,
    posterPath = selectPosterPath(),
    rating = movieInfoResponse.voteAverage,
    votes = movieInfoResponse.voteAverage,
    releaseDate = movieInfoResponse.releaseDate?.convertIntoData(),
    runtime = movieInfoResponse.runtime,
    genres = movieInfoResponse.genres,
    actors = movieCast,
    storyline = movieInfoResponse.overview,
    tagLine = movieInfoResponse.tagLine,
    budget = movieInfoResponse.budget,
    revenue = movieInfoResponse.revenue,
    homePage = movieInfoResponse.homepage,
    reviews = movieReviewsResponse.reviewDetails
)
