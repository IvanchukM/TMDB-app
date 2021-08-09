package com.example.themoviedb.ui.account.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.themoviedb.repository.MovieRepository
import com.example.themoviedb.repository.SharedPreferencesRepository
import com.example.themoviedb.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okio.ByteString.Companion.toByteString
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) :
    BaseViewModel() {

    val loginResponse = MutableLiveData<Boolean>()

    fun loginUser(username: String, password: String) {
        compositeDisposable.add(
            repository.getRequestToken()
                .flatMap { requestToken ->
                    repository.loginUser(username, password, requestToken.requestToken)
                        .flatMap { accessToken ->
                            repository.createSession(accessToken.accessToken)
                        }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { success ->
                        saveSessionId(success.sessionId)
                        loginResponse.value = true
                        saveUsername(username)
                    },
                    { error ->
                        Log.d("error", error.toString())
                        loginResponse.value = false
                    }
                )
        )
    }

    private fun saveUsername(username: String) {
        sharedPreferencesRepository.saveUsername(username = username)
    }

    private fun saveSessionId(sessionId: String) {
        sharedPreferencesRepository.saveSessionId(sessionId)
    }
}
