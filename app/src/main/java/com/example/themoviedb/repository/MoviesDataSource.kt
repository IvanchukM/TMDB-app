package com.example.themoviedb.repository

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.models.movies.MoviesResponse
import com.example.themoviedb.services.MoviesService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesDataSource @Inject constructor(
    private val moviesService: MoviesService,
    private val queryType: MovieQueryType
) :
    RxPagingSource<Int, MoviesModel>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, MoviesModel>> {

        val position = params.key ?: 1

        return when (queryType) {
            is MovieQueryType.NowPlaying ->
                moviesService.getNowPlayingMovies(position)
            is MovieQueryType.Popular ->
                moviesService.getPopularMovies(position)
            is MovieQueryType.TopRated ->
                moviesService.getTopRatedMovies(position)
            is MovieQueryType.SearchString -> moviesService.searchMovieByName(
                queryType.searchString,
                position
            )
        }
            .subscribeOn(Schedulers.io())
            .map { toLoadResult(it, position) }
            .onErrorReturn { error -> LoadResult.Error(error) }
    }

    private fun toLoadResult(
        data: MoviesResponse,
        position: Int
    ): LoadResult<Int, MoviesModel> {

        return LoadResult.Page(
            data = data.movieModel,
            prevKey = if (position == 1) null else position - 1,
            nextKey = if (position >= data.totalPages) null else position + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, MoviesModel>): Int {
        return 1
    }
}
