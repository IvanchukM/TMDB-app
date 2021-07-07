package com.example.themoviedb.ui

import androidx.lifecycle.MutableLiveData
import com.example.themoviedb.utils.BaseViewModel
import com.example.themoviedb.utils.NetworkHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    networkHandler: NetworkHandler
) : BaseViewModel() {

    val isInternetAvailable = MutableLiveData<Boolean>()

    init {
        compositeDisposable.add(
            networkHandler.observeNetworkState()
                .subscribe {
                    isInternetAvailable.postValue(it)
                }
        )
    }
}
