package com.example.themoviedb

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.themoviedb.repository.SharedPreferencesRepository
import com.example.themoviedb.utils.ApplicationThemes
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TheMovieDBApplication : Application() {
    @Inject
    lateinit var sharedPreferencesRepository: SharedPreferencesRepository
    override fun onCreate() {
        super.onCreate()

        setUpTheme()
    }

    private fun setUpTheme() {
        when (sharedPreferencesRepository.loadApplicationThemeSync()) {
            ApplicationThemes.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            ApplicationThemes.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            ApplicationThemes.AUTO -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

}



