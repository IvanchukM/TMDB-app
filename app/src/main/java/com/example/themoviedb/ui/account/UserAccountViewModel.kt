package com.example.themoviedb.ui.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.themoviedb.models.favorite_movies.FavoriteMovieModel
import com.example.themoviedb.repository.MovieRepository
import com.example.themoviedb.repository.SharedPreferencesRepository
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
    private val sharedPreferencesRepository: SharedPreferencesRepository
) : BaseViewModel() {

    val favoriteMovies = MutableLiveData<PagingData<FavoriteMovieModel>>()

    fun getFavoriteMovies() {
        val sessionId = sharedPreferencesRepository.getSessionId()
        val username = sharedPreferencesRepository.getUsername()
        compositeDisposable.add(
            repository.getFavoriteMovies(sessionId,username )
                .performCallWhenInternetIsAvailable(networkHandler.observeNetworkState())
                .cachedIn(viewModelScope)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    favoriteMovies.value = result
                }
        )

    }

}