package com.example.themoviedb.repository

import android.content.SharedPreferences
import com.example.themoviedb.utils.ApplicationThemes
import com.example.themoviedb.utils.LayoutManagerTypes
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

private const val LAYOUT_MANAGER = "layoutManager"
private const val APPLICATION_THEME = "applicationTheme"
private const val SESSION_ID = "session_id"

@Singleton
class SettingsRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveLayoutManagerType(currentLayoutManagerType: LayoutManagerTypes): Completable =
        Completable.fromCallable {
            sharedPreferences.edit()
                .putString(LAYOUT_MANAGER, currentLayoutManagerType.name)
                .apply()
        }
            .subscribeOn(Schedulers.io())

    fun loadLayoutManagerType(): Single<LayoutManagerTypes> {
        val recyclerState = sharedPreferences.getString(
            LAYOUT_MANAGER,
            null
        ) ?: LayoutManagerTypes.LINEAR.name

        return Single.fromCallable { LayoutManagerTypes.enumToString(recyclerState) }
            .subscribeOn(Schedulers.io())
    }

    fun saveApplicationTheme(currentApplicationTheme: ApplicationThemes): Completable =
        Completable.fromCallable {
            sharedPreferences.edit()
                .putString(APPLICATION_THEME, currentApplicationTheme.name)
                .apply()
        }
            .subscribeOn(Schedulers.io())

    fun loadApplicationTheme(): Single<ApplicationThemes> {
        val applicationTheme = sharedPreferences.getString(
            APPLICATION_THEME, null
        ) ?: ApplicationThemes.AUTO.name

        return Single.fromCallable {
            ApplicationThemes.enumToString(applicationTheme)
        }
            .subscribeOn(Schedulers.io())
    }

    fun loadApplicationThemeSync(): ApplicationThemes {
        val applicationTheme = sharedPreferences.getString(
            APPLICATION_THEME, null
        ) ?: ApplicationThemes.AUTO.name

        return ApplicationThemes.enumToString(applicationTheme)

    }

    fun saveSessionId(sessionId: String): Completable =
        Completable.fromCallable {
            sharedPreferences.edit()
                .putString(SESSION_ID, sessionId)
                .apply()
        }
            .subscribeOn(Schedulers.io())

    fun loadSessionId(): Single<String?> {
        val sessionId = sharedPreferences.getString(SESSION_ID, null)
        return Single.fromCallable {
            sessionId
        }
            .subscribeOn(Schedulers.io())
    }
}
