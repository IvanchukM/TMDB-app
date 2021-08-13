package com.example.themoviedb.ui.account

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.themoviedb.models.account_movies.AccountMovieModel
import com.example.themoviedb.repository.AccountQueryType
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

    val movieData = MutableLiveData<PagingData<AccountMovieModel>>()
    val currentQuery = MutableLiveData<AccountQueryType>()

    fun loadAccountMovies(queryType: AccountQueryType) {
        val sessionId = sharedPreferencesRepository.getSessionId()
        val username = sharedPreferencesRepository.getUsername()
        compositeDisposable.add(
            repository.getFavoriteMovies(sessionId.toString(), username, queryType)
                .performCallWhenInternetIsAvailable(networkHandler.observeNetworkState())
                .cachedIn(viewModelScope)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result ->
                    movieData.value = result
                    currentQuery.value = queryType
                }
        )
    }

    fun logoutUser() {
        compositeDisposable.add(
            repository.deleteSession(sharedPreferencesRepository.getSessionId().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { success ->
                        Log.d("logout", "success $success")
                        sharedPreferencesRepository.saveSessionId(null)
                    },
                    { error ->
                        Log.d("logout", "error $error ")
                    }
                )
        )
    }

}