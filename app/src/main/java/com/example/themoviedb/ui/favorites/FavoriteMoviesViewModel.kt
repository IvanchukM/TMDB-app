package com.example.themoviedb.ui.favorites

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.themoviedb.database.FavoriteMovieEntity
import com.example.themoviedb.repository.MovieRepository
import com.example.themoviedb.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class FavoriteMoviesViewModel @Inject constructor(private val repository: MovieRepository) :
    BaseViewModel() {

    val favoriteMovies = MutableLiveData<List<FavoriteMovieEntity>>()

    private fun fetchFavoriteMovies() {
        compositeDisposable.add(
            repository.getFavoriteMovies()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    favoriteMovies.postValue(result)
                }, { error ->
                    Log.d("TAG", error.toString())
                })
        )
    }
}
