package com.example.themoviedb.repository

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.themoviedb.models.favorite_movies.FavoriteMovieModel
import com.example.themoviedb.models.favorite_movies.FavoriteMoviesResponse
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.models.movies.MoviesResponse
import com.example.themoviedb.services.MoviesService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

class FavoriteMoviesDataSource(
    private val moviesService: MoviesService,
    private val sessionId: String,
    private val username: String
) : RxPagingSource<Int, FavoriteMovieModel>() {


    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, FavoriteMovieModel>> {
        val position = params.key ?: 1

        return moviesService.getFavoriteMovies(username, sessionId, position)
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it, position) }
            .onErrorReturn { error -> LoadResult.Error(error) }
    }

    private fun toLoadResult(
        data: FavoriteMoviesResponse,
        position: Int
    ): LoadResult<Int, FavoriteMovieModel> {

        return LoadResult.Page(
            data = data.favoriteMovieModels,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position >= data.totalPages) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, FavoriteMovieModel>): Int {
        return 1
    }

}