package com.example.themoviedb.ui.movie_details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.themoviedb.models.movie_details.MovieCast
import com.example.themoviedb.models.movie_details.MovieDetailsResponse
import com.example.themoviedb.models.movie_reviews.ReviewDetails
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.repository.MovieRepository
import com.example.themoviedb.utils.BaseViewModel
import com.example.themoviedb.utils.LoadingState
import com.example.themoviedb.utils.NetworkHandler
import com.example.themoviedb.utils.extensions.performCallWhenInternetIsAvailable
import com.example.themoviedb.utils.handleLoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val networkHandler: NetworkHandler
) : BaseViewModel() {

    val movieData = MutableLiveData<MovieDetailsResponse>()
    val movieReviews = MutableLiveData<List<ReviewDetails>>()
    val movieActorsData = MutableLiveData<MovieCast>()
    val loadingState = MutableLiveData<LoadingState>()
    val movieCommonData = MutableLiveData<MoviesModel>()

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
}
