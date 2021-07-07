package com.example.themoviedb.utils.extensions

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

fun <T> Single<T>.performCallWhenInternetIsAvailable(networkStateObservable: Observable<Boolean>): Single<T> =
    networkStateObservable
        .filter { it }
        .firstOrError()
        .flatMap { this }
        .retryWhen { errors ->
            errors.flatMap {
                if (it !is ConnectException && it !is UnknownHostException) {
                    throw it
                } else {
                    Flowable.timer(5, TimeUnit.SECONDS)
                }
            }
        }

fun <T> Flowable<T>.performCallWhenInternetIsAvailable(networkStateObservable: Observable<Boolean>): Flowable<T> =
    networkStateObservable
        .filter { it }
        .firstOrError()
        .flatMapPublisher { this }
        .retryWhen { errors ->
            errors.flatMap { error ->
                if (error !is ConnectException && error !is UnknownHostException) {
                    throw error
                } else {
                    Flowable.timer(5, TimeUnit.SECONDS)
                }
            }
        }
