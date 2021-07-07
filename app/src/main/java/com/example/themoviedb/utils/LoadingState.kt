package com.example.themoviedb.utils

import androidx.lifecycle.MutableLiveData
import io.reactivex.Single

sealed class LoadingState {
    object Load : LoadingState()
    object Success : LoadingState()
    class Error(throwable: Throwable) : LoadingState()
}

fun <T> Single<T>.handleLoadingState(liveData: MutableLiveData<LoadingState>): Single<T> =
    this.doOnSubscribe { liveData.postValue(LoadingState.Load) }
        .doOnSuccess { liveData.postValue(LoadingState.Success) }
        .doOnError { liveData.postValue(LoadingState.Error(it)) }
