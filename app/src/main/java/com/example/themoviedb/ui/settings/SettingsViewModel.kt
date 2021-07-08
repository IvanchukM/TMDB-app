package com.example.themoviedb.ui.settings

import androidx.lifecycle.MutableLiveData
import com.example.themoviedb.repository.SettingsRepository
import com.example.themoviedb.utils.ApplicationThemes
import com.example.themoviedb.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val settingsRepository: SettingsRepository) :
    BaseViewModel() {
    val applicationTheme = MutableLiveData<ApplicationThemes>()

    init {
        compositeDisposable.add(
            settingsRepository.loadApplicationTheme()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { theme -> applicationTheme.postValue(theme) }
        )
    }

    fun updateApplicationTheme(applicationThemes: ApplicationThemes) {
        compositeDisposable.add(
            settingsRepository.saveApplicationTheme(applicationThemes)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
        applicationTheme.postValue(applicationThemes)
    }

}
