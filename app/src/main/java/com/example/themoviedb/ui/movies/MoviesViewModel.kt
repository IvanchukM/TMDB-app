package com.example.themoviedb.ui.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.repository.MovieQueryType
import com.example.themoviedb.repository.MovieRepository
import com.example.themoviedb.repository.SharedPreferencesRepository
import com.example.themoviedb.utils.BaseViewModel
import com.example.themoviedb.utils.LayoutManagerTypes
import com.example.themoviedb.utils.NetworkHandler
import com.example.themoviedb.utils.extensions.performCallWhenInternetIsAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val networkHandler: NetworkHandler,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : BaseViewModel() {

    val movies = MutableLiveData<PagingData<MoviesModel>>()
    val layoutState = MutableLiveData<LayoutManagerTypes>()
    val isUserLoginIn = MutableLiveData<Boolean>()

    init {
        compositeDisposable.add(
            sharedPreferencesRepository.loadLayoutManagerType()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { type -> layoutState.postValue(type) }
        )

    }

    fun checkIfUserLoginIn() {
        isUserLoginIn.value = sharedPreferencesRepository.getSessionId().isNullOrEmpty()
    }

    fun updateLayoutManagerType() {
        val layoutManagerType = when (layoutState.value) {
            LayoutManagerTypes.GRID -> LayoutManagerTypes.LINEAR
            LayoutManagerTypes.LINEAR -> LayoutManagerTypes.GRID
            else -> LayoutManagerTypes.LINEAR
        }
        compositeDisposable.add(
            sharedPreferencesRepository.saveLayoutManagerType(layoutManagerType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
        layoutState.postValue(layoutManagerType)
    }

    fun loadMovies(queryType: MovieQueryType) {
        compositeDisposable.add(
            repository.getMovies(queryType)
                .performCallWhenInternetIsAvailable(networkHandler.observeNetworkState())
                .cachedIn(viewModelScope)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    movies.value = result
                }
        )
    }
}
