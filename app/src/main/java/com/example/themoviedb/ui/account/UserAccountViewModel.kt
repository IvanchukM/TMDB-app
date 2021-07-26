package com.example.themoviedb.ui.account

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.themoviedb.models.favorite_movies.FavoriteMovieModel
import com.example.themoviedb.models.favorite_movies.FavoriteMoviesResponse
import com.example.themoviedb.repository.MovieRepository
import com.example.themoviedb.repository.SettingsRepository
import com.example.themoviedb.utils.BaseViewModel
import com.example.themoviedb.utils.NetworkHandler
import com.example.themoviedb.utils.extensions.performCallWhenInternetIsAvailable
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val networkHandler: NetworkHandler,
    private val settingsRepository: SettingsRepository
) : BaseViewModel() {
    private var sessionId: String? = null
    val favoriteMovies = MutableLiveData<PagingData<FavoriteMovieModel>>()

    init {
        getSessionId()
    }

    fun getFavoriteMovies() {
        compositeDisposable.add(
            repository.getFavoriteMovies(sessionId.toString())
                .performCallWhenInternetIsAvailable(networkHandler.observeNetworkState())
                .cachedIn(viewModelScope)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    favoriteMovies.value = result
                }
        )

    }

    private fun getSessionId() {
        compositeDisposable.add(
            settingsRepository.loadSessionId()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ sessionId ->
                    this.sessionId = sessionId
                    Log.d("TAG", "getSessionId: $sessionId ")
                }, { error ->
                    Log.d("error", error.toString())
                })
        )
    }
}