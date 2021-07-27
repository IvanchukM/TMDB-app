package com.example.themoviedb.ui.account.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.themoviedb.models.auth.AuthResponse
import com.example.themoviedb.repository.MovieRepository
import com.example.themoviedb.repository.SharedPreferencesRepository
import com.example.themoviedb.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MovieRepository,
    private val sharedPreferencesRepository: SharedPreferencesRepository
) :
    BaseViewModel() {

    val loginResponse = MutableLiveData<Boolean>()
    val loginState = MutableLiveData<Boolean>()
    val username = MutableLiveData<String>()

    init {
        getLoginState()
        username.postValue(getUsername())
    }

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
                        saveLoginState(true)
                        saveUsername(username)
                    },
                    { error ->
                        Log.d("error", error.toString())
                        loginResponse.value = false
                    }
                )
        )
    }

    fun logoutUser() {
        compositeDisposable.add(
            repository.deleteSession(sharedPreferencesRepository.getSessionId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { success ->
                        Log.d("logout", "success $success")
                        saveLoginState(false)
                    },
                    { error ->
                        Log.d("logout", "error $error ")
                    }
                )
        )
    }

    private fun saveLoginState(loginState: Boolean) {
        sharedPreferencesRepository.saveLoginState(isLogged = loginState)
    }

    private fun saveUsername(username: String) {
        sharedPreferencesRepository.saveUsername(username = username)
    }

    private fun getUsername(): String =
        sharedPreferencesRepository.getUsername()

    fun getLoginState() {
        loginState.postValue(sharedPreferencesRepository.getLoginState())
    }


    //    private fun getRequestToken() {
//        compositeDisposable.add(
//            repository.getRequestToken()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ token -> requestToken.postValue(token) }, {
//                    Log.d("TAG", "error $it: ")
//                })
//        )
//    }

//    fun performLogin(username: String, password: String) {
//        getRequestToken()
//        val requestToken = requestToken.value?.requestToken
//        if (requestToken != null) {
//            compositeDisposable.add(
//                repository.loginUser(username, password, requestToken)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe({ accessToken ->
//                        createSession(accessToken.accessToken)
//                        loginResponse.value = accessToken.success
////                        this.accessToken.postValue(accessToken)
//                    }, {
//                        Log.d("error happened", it.toString())
//                        loginResponse.value = false
//
//                    })
//            )
//        }
//    }

//    private fun createSession(accessToken: String) {
//        compositeDisposable.add(
//            repository.createSession(accessToken)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ sessionId ->
//                    if (sessionId.success) {
//                        saveSessionId(sessionId.sessionId)
//                    } else {
//                        Log.d("TAG", "invalid session id")
//                    }
//                    Log.d("TAG", "sessionId: $sessionId ")
//                }, {})
//        )
//    }

    private fun saveSessionId(sessionId: String) {
        sharedPreferencesRepository.saveSessionId(sessionId)
    }
}
