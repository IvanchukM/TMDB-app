package com.example.themoviedb.ui.account.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.themoviedb.models.auth.AuthResponse
import com.example.themoviedb.models.auth.LoginResponse
import com.example.themoviedb.models.auth.SessionResponse
import com.example.themoviedb.repository.MovieRepository
import com.example.themoviedb.repository.SettingsRepository
import com.example.themoviedb.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val settingsRepository: SettingsRepository
) :
    BaseViewModel() {

    private val requestToken = MutableLiveData<AuthResponse>()
    //    private val accessToken = MutableLiveData<LoginResponse>()

    private fun getRequestToken() {
        compositeDisposable.add(
            repository.getRequestToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ token -> requestToken.postValue(token) }, {
                    Log.d("TAG", "error $it: ")
                })
        )
    }

    fun performLogin(username: String, password: String) {
        getRequestToken()
        val requestToken = requestToken.value?.requestToken
        if (requestToken != null) {
            compositeDisposable.add(
                repository.loginUser(username, password, requestToken)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ accessToken ->
                        if (accessToken.success) {
                            createSession(accessToken.accessToken)
                        } else {
                            Log.d("TAG", "invalid access token")
                        }
//                        this.accessToken.postValue(accessToken)
                    }, {
                        Log.d("TAG", it.toString())
                    })
            )
        }
    }

    private fun createSession(accessToken: String) {
        compositeDisposable.add(
            repository.createSession(accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ sessionId ->
                    if (sessionId.success) {
                        saveSessionId(sessionId.sessionId)
                    } else {
                        Log.d("TAG", "invalid session id")
                    }
                    Log.d("TAG", "sessionId: $sessionId ")
                }, {})
        )
    }

    private fun saveSessionId(sessionId: String) {
        compositeDisposable.add(
            settingsRepository.saveSessionId(sessionId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }
}
