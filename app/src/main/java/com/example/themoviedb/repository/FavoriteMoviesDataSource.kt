package com.example.themoviedb.repository

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.themoviedb.models.account_movies.AccountMovieModel
import com.example.themoviedb.models.account_movies.AccountMoviesResponse
import com.example.themoviedb.services.MoviesService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class FavoriteMoviesDataSource(
    private val moviesService: MoviesService,
    private val sessionId: String,
    private val username: String,
    private val queryType: AccountQueryType
) : RxPagingSource<Int, AccountMovieModel>() {


    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, AccountMovieModel>> {
        val position = params.key ?: 1

        return when (queryType) {
            is AccountQueryType.Favorite -> moviesService.getFavoriteMovies(
                username,
                sessionId,
                position
            )
            is AccountQueryType.Rated -> moviesService.getRatedMovies(
                username,
                sessionId,
                position
            )
            is AccountQueryType.Watchlist -> moviesService.getWatchlist(
                username,
                sessionId,
                position
            )
        }
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it, position) }
            .onErrorReturn { error -> LoadResult.Error(error) }
    }

    private fun toLoadResult(
        data: AccountMoviesResponse,
        position: Int
    ): LoadResult<Int, AccountMovieModel> {

        return LoadResult.Page(
            data = data.accountMoviesModel,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position >= data.totalPages) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, AccountMovieModel>): Int {
        return 1
    }

}