package com.example.themoviedb.ui.movie_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.themoviedb.models.account_movies.*
import com.example.themoviedb.models.movie_details.MovieCast
import com.example.themoviedb.models.movie_details.MovieDetailsResponse
import com.example.themoviedb.models.movie_reviews.ReviewDetails
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.repository.MovieRepository
import com.example.themoviedb.repository.SharedPreferencesRepository
import com.example.themoviedb.utils.*
import com.example.themoviedb.utils.extensions.performCallWhenInternetIsAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val networkHandler: NetworkHandler,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : BaseViewModel() {

    val movieData = MutableLiveData<MovieDetailsResponse>()
    val movieReviews = MutableLiveData<List<ReviewDetails>>()
    val movieActorsData = MutableLiveData<MovieCast>()
    val loadingState = MutableLiveData<LoadingState>()
    val movieCommonData = MutableLiveData<MoviesModel>()
    val isUserLoginIn = MutableLiveData<Boolean>()
    val movieState = MutableLiveData<AccountMovieStateResponse>()
    val testToast = MutableLiveData<String>()

    init {
        checkIfUserLoginIn()
    }

    fun setMovieData(movieModel: MoviesModel) {
        movieCommonData.postValue(movieModel)
        compositeDisposable.add(
            repository.getAllMovieData(movieModel.id)
                .performCallWhenInternetIsAvailable(networkHandler.observeNetworkState())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .handleLoadingState(loadingState)
                .subscribe({ result ->
                    movieData.value = result.movieInfoResponse
                    movieActorsData.value = result.movieCast
                    movieReviews.value = result.movieReviewsResponse.reviewDetails
                }, {
                    Log.d("TAG", it.toString())
                })
        )
    }

    fun getMovieState(movieId: Int) {
        val sessionId = sharedPreferencesRepository.getSessionId()
        compositeDisposable.add(
            repository.getAccountMovieState(movieId, sessionId.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ movieState ->
                    this.movieState.value = movieState
                }, { error ->
                    Log.d("movie state error", "error happened $error ")
                })
        )
    }

    fun switchMovieFavoriteState(toggleFavoriteMovieStateModel: ToggleFavoriteMovieStateModel) {
        val username = sharedPreferencesRepository.getUsername()
        val sessionId = sharedPreferencesRepository.getSessionId()
        compositeDisposable.add(
            repository.switchFavoriteMovieState(
                username,
                sessionId.toString(),
                toggleFavoriteMovieStateModel
            ).map {
                getMovieState(toggleFavoriteMovieStateModel.movieId)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (!toggleFavoriteMovieStateModel.isFavorite) {
                        testToast.value = "Removed from favorite"
                    } else {
                        testToast.value = "Added to favorite"
                    }
                }, { error ->
                    Log.d("error", "error happened $error ")
                })
        )
    }

    fun switchWatchlistState(toggleWatchlistStateModel: ToggleWatchlistStateModel) {
        val username = sharedPreferencesRepository.getUsername()
        val sessionId = sharedPreferencesRepository.getSessionId()
        compositeDisposable.add(
            repository.toggleWatchlistState(
                username,
                sessionId.toString(),
                toggleWatchlistStateModel
            ).map {
                getMovieState(toggleWatchlistStateModel.movieId)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (!toggleWatchlistStateModel.isInWatchlist) {
                        testToast.value = "Removed from watchlist"
                    } else {
                        testToast.value = "Added to watchlist"
                    }
                }, { error ->
                    Log.d("error", "error happened $error ")
                })
        )
    }

    fun setMovieRating(movieId: Int, movieRating: Float) {
        val sessionId = sharedPreferencesRepository.getSessionId()
        compositeDisposable.add(
            repository.setMovieRating(
                movieId,
                sessionId.toString(),
                MovieRating(isMovieRated = true, rating = movieRating)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    testToast.value = "Rating changed to $movieRating"
                }, { error ->
                    Log.d("error", "error happened $error ")
                })
        )
    }

    private fun checkIfUserLoginIn() {
        isUserLoginIn.value = !sharedPreferencesRepository.getSessionId().isNullOrEmpty()
    }
}
