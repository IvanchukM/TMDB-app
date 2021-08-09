package com.example.themoviedb.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.themoviedb.models.MovieInfoModel
import com.example.themoviedb.models.account_movies.*
import com.example.themoviedb.models.auth.AuthResponse
import com.example.themoviedb.models.auth.DeleteSessionResponse
import com.example.themoviedb.models.auth.LoginResponse
import com.example.themoviedb.models.auth.SessionResponse
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.services.MoviesService
import io.reactivex.Flowable
import io.reactivex.Single
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

    fun getFavoriteMovies(
        sessionId: String,
        username: String,
        queryType: AccountQueryType
    ): Flowable<PagingData<AccountMovieModel>> = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = {
            FavoriteMoviesDataSource(
                moviesService,
                sessionId,
                username,
                queryType
            )
        }
    ).flowable

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

    fun deleteSession(sessionId: String): Single<DeleteSessionResponse> =
        moviesService.deleteSession(sessionId)

    fun getAccountMovieState(movieId: Int, sessionId: String) =
        moviesService.getAccountMovieSate(movieId, sessionId)

    fun switchFavoriteMovieState(
        username: String,
        sessionId: String,
        toggleFavoriteMovieStateModel: ToggleFavoriteMovieStateModel
    ): Single<ToggleMovieStateResponse> =
        moviesService.toggleFavoriteMovie(
            username,
            sessionId,
            toggleFavoriteMovieStateModel
        )

    fun toggleWatchlistState(
        username: String,
        sessionId: String,
        toggleWatchlistStateModel: ToggleWatchlistStateModel
    ): Single<ToggleMovieStateResponse> =
        moviesService.toggleWatchlist(
            username, sessionId, toggleWatchlistStateModel
        )

    fun setMovieRating(
        movieId: Int,
        sessionId: String,
        movieRating: MovieRating
    ): Single<SetMovieRatingResponse> =
        moviesService.setMovieRating(movieId, sessionId, movieRating)
}
