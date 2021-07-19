package com.example.themoviedb.ui.movie_details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.themoviedb.models.MovieCommonDataModel
import com.example.themoviedb.models.MovieDetailsModel
import com.example.themoviedb.models.MovieInfoModel
import com.example.themoviedb.models.toMovieDetailsModel
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

    val movieDetails = MutableLiveData<MovieDetailsModel>()
    val loadingState = MutableLiveData<LoadingState>()
    val movieCommonData = MutableLiveData<MovieCommonDataModel>()
    var test = MutableLiveData<MovieInfoModel>()

//    fun addFavoriteMovie() {
//        test.value?.let {
//            compositeDisposable.add(
//                repository.addFavoriteMovie(it.toFavoriteEntity())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({
//                    }, { error ->
//                        Log.d("TAG", "addFavoriteMovie: $error")
//                    })
//            )
//        }
//    }
    fun setMovieData(
        movieCommonDataModel: MovieCommonDataModel
    ) {
        movieCommonData.postValue(movieCommonDataModel)
            compositeDisposable.add(
                repository.getAllMovieData(movieCommonDataModel.movieId)
                    .performCallWhenInternetIsAvailable(networkHandler.observeNetworkState())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .handleLoadingState(loadingState)
                    .subscribe({ result ->
                        movieDetails.value = result.toMovieDetailsModel()
                        test.postValue(result)
                    }, {
                        Log.d("TAG", it.toString())
                    })
            )
    }
}
