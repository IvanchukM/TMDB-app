package com.example.themoviedb.services

import com.example.themoviedb.models.account_movies.*
import com.example.themoviedb.models.auth.AuthResponse
import com.example.themoviedb.models.auth.DeleteSessionResponse
import com.example.themoviedb.models.auth.LoginResponse
import com.example.themoviedb.models.auth.SessionResponse
import com.example.themoviedb.models.movie_details.MovieCast
import com.example.themoviedb.models.movie_details.MovieDetailsResponse
import com.example.themoviedb.models.movies.MoviesResponse
import com.example.themoviedb.models.movie_reviews.MovieReviewsResponse
import io.reactivex.Single
import retrofit2.http.*

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

    @GET("3/authentication/token/new")
    fun getRequestToken(): Single<AuthResponse>

    @POST("3/authentication/token/validate_with_login")
    fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("request_token") requestToken: String
    ): Single<LoginResponse>

    @GET("/3/authentication/session/new")
    fun createSession(
        @Query("request_token") accessToken: String
    ): Single<SessionResponse>

    @DELETE("3/authentication/session")
    fun deleteSession(
        @Query("session_id") sessionId: String
    ): Single<DeleteSessionResponse>

    @POST("/3/account/{username}/favorite")
    fun toggleFavoriteMovie(
        @Path("username") username: String,
        @Query("session_id") sessionId: String,
        @Body toggleFavoriteMovieStateModel: ToggleFavoriteMovieStateModel
    ): Single<ToggleMovieStateResponse>

    @POST("/3/account/{username}/watchlist")
    fun toggleWatchlist(
        @Path("username") username: String,
        @Query("session_id") sessionId: String,
        @Body toggleWatchlistStateModel: ToggleWatchlistStateModel
    ): Single<ToggleMovieStateResponse>

    @POST("/3/movie/{movie_id}/rating")
    fun setMovieRating(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String,
        @Body movieRating: MovieRating
    ): Single<SetMovieRatingResponse>

    @GET("3/account/{username}/favorite/movies")
    fun getFavoriteMovies(
        @Path("username") username: String,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int
    ): Single<AccountMoviesResponse>

    @GET("3/account/{username}/rated/movies")
    fun getRatedMovies(
        @Path("username") username: String,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int
    ): Single<AccountMoviesResponse>

    @GET("3/account/{username}/watchlist/movies")
    fun getWatchlist(
        @Path("username") username: String,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int
    ): Single<AccountMoviesResponse>

    @GET("3/movie/{movie_id}/account_states")
    fun getAccountMovieSate(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String
    ): Single<AccountMovieStateResponse>
}
