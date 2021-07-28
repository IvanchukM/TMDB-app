package com.example.themoviedb.ui.movies

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.repository.MovieQueryType
import com.example.themoviedb.repository.MovieRepository
import com.example.themoviedb.repository.SettingsRepository
import com.example.themoviedb.utils.ApplicationThemes
import com.example.themoviedb.utils.BaseViewModel
import com.example.themoviedb.utils.LayoutManagerTypes
import com.example.themoviedb.utils.NetworkHandler
import com.example.themoviedb.utils.extensions.performCallWhenInternetIsAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

private const val NOW_PLAYING_STRING = "Now Playing Movies"
private const val TOP_RATED_STRING = "Top Rated Movies"
private const val POPULAR_STRING = "Popular Movies"
private const val MOVIES_STRING = "Movies"

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val networkHandler: NetworkHandler,
    private val settingsRepository: SettingsRepository
) : BaseViewModel() {

    val movies = MutableLiveData<PagingData<MoviesModel>>()
    val layoutState = MutableLiveData<LayoutManagerTypes>()

    init {
        compositeDisposable.add(
            settingsRepository.loadLayoutManagerType()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { type -> layoutState.postValue(type) }
        )

    }

    fun updateLayoutManagerType() {
        val layoutManagerType = when (layoutState.value) {
            LayoutManagerTypes.GRID -> LayoutManagerTypes.LINEAR
            LayoutManagerTypes.LINEAR -> LayoutManagerTypes.GRID
            else -> LayoutManagerTypes.LINEAR
        }
        compositeDisposable.add(
            settingsRepository.saveLayoutManagerType(layoutManagerType)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
        layoutState.postValue(layoutManagerType)
    }

    fun loadMovies(queryType: MovieQueryType) {
        when (queryType) {
            MovieQueryType.Popular -> POPULAR_STRING
            MovieQueryType.NowPlaying -> NOW_PLAYING_STRING
            MovieQueryType.TopRated -> TOP_RATED_STRING
            else -> MOVIES_STRING
        }
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
