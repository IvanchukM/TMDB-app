package com.example.themoviedb.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.themoviedb.models.MovieInfoModel
import com.example.themoviedb.models.auth.AuthResponse
import com.example.themoviedb.models.auth.LoginResponse
import com.example.themoviedb.models.auth.SessionResponse
import com.example.themoviedb.models.favorite_movies.AddFavoriteMovieResponse
import com.example.themoviedb.models.favorite_movies.FavoriteMovieModel
import com.example.themoviedb.models.favorite_movies.FavoriteMoviesResponse
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.services.MoviesService
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val moviesService: MoviesService
) {

    fun getMovies(queryType: MovieQueryType): Flowable<PagingData<MoviesModel>> = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { MoviesDataSource(moviesService, queryType) }
    ).flowable

    fun getFavoriteMovies(sessionId: String): Flowable<PagingData<FavoriteMovieModel>> = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { FavoriteMoviesDataSource(moviesService, sessionId,"brestbuble") }
    ).flowable

//    fun getFavoriteMovies(username: String, sessionId: String): Single<FavoriteMoviesResponse> =
//        moviesService.getFavoriteMovies(
//            username = username,
//            sessionId = sessionId
//        )

    fun getAllMovieData(movieId: Int): Single<MovieInfoModel> {
        return Single.zip(
            moviesService.getMovieInfo(movieId),
            moviesService.getMoviesReviews(movieId),
            moviesService.getMovieCast(movieId),
            { data, reviews, cast ->
                MovieInfoModel(data, reviews, cast)
            }
        )
    }

    fun getRequestToken(): Single<AuthResponse> =
        moviesService.getRequestToken()

    fun loginUser(username: String, password: String, requestToken: String): Single<LoginResponse> =
        moviesService.loginUser(username, password, requestToken)

    fun createSession(accessToken: String): Single<SessionResponse> =
        moviesService.createSession(accessToken)

    fun addFavoriteMovie(
        username: String,
        sessionId: String,
        movieId: Int
    ): Single<AddFavoriteMovieResponse> =
        moviesService.addFavoriteMovie(
            username = username,
            sessionId = sessionId,
            mediaType = "movie",
            movieId = movieId,
            favoriteFlag = true
        )

    fun removeFavoriteMovie(
        username: String,
        sessionId: String,
        movieId: Int
    ): Single<AddFavoriteMovieResponse> =
        moviesService.addFavoriteMovie(
            username = username,
            sessionId = sessionId,
            mediaType = "movie",
            movieId = movieId,
            favoriteFlag = false
        )
}
