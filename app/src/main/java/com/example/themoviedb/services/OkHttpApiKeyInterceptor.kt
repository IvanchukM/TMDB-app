package com.example.themoviedb.services

import com.example.themoviedb.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY = "api_key"

class OkHttpApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder().url(
                chain.request().url.newBuilder()
                    .addQueryParameter(API_KEY, BuildConfig.MOVIE_KEY).build()
            ).build()
        )
    }
}
