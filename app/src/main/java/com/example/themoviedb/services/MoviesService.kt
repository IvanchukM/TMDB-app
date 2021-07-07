package com.example.themoviedb.services

import com.example.themoviedb.models.movie_details.MovieCast
import com.example.themoviedb.models.movie_details.MovieDetailsResponse
import com.example.themoviedb.models.movies.MoviesResponse
import com.example.themoviedb.models.movie_reviews.MovieReviewsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("3/search/movie")
    fun searchMovieByName(
        @Query("query") searchString: String,
        @Query("page") page: Int
    ): Single<MoviesResponse>

    @GET("3/movie/top_rated")
    fun getTopRatedMovies(
        @Query("page") page: Int
    ): Single<MoviesResponse>

    @GET("3/movie/popular")
    fun getPopularMovies(
        @Query("page") page: Int
    ): Single<MoviesResponse>

    @GET("3/movie/now_playing")
    fun getNowPlayingMovies(
        @Query("page") page: Int
    ): Single<MoviesResponse>

    @GET("3/movie/{id}")
    fun getMovieInfo(
        @Path("id") movieId: Int,
    ): Single<MovieDetailsResponse>

    @GET("3/movie/{id}/reviews")
    fun getMoviesReviews(
        @Path("id") movieId: Int,
    ): Single<MovieReviewsResponse>

    @GET("3/movie/{id}/credits")
    fun getMovieCast(
        @Path("id") movieId: Int,
    ): Single<MovieCast>
}
