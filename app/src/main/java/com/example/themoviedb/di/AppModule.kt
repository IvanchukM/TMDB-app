package com.example.themoviedb.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.themoviedb.BuildConfig
import com.example.themoviedb.database.DB_NAME
import com.example.themoviedb.database.FavoriteMovieDao
import com.example.themoviedb.database.FavoriteMoviesDatabase
import com.example.themoviedb.services.MoviesService
import com.example.themoviedb.services.OkHttpApiKeyInterceptor
import com.example.themoviedb.utils.DateDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

private const val SHARED_PREFERENCES = "sharedPreferences"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(
            SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )

    @Provides
    @Singleton
    fun provideFavoriteMoviesDb(context: Context): FavoriteMoviesDatabase =
        Room.databaseBuilder(context, FavoriteMoviesDatabase::class.java, DB_NAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesFavoriteMoviesDao(context: Context): FavoriteMovieDao =
        provideFavoriteMoviesDb(context).movieDao()

    @Provides
    @Singleton
    fun providesOkHttpApiKeyInterceptor(): Interceptor {
        return OkHttpApiKeyInterceptor()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(apiKeyInterceptor: Interceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesGsonDeserializer(): Gson = GsonBuilder().registerTypeAdapter(
        Date::class.java,
        DateDeserializer
    ).create()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun providesMovieApi(retrofit: Retrofit): MoviesService =
        retrofit.create(MoviesService::class.java)
}
