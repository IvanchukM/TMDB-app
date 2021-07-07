package com.example.themoviedb.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.themoviedb.models.MovieInfoModel
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.services.MoviesService
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val moviesService: MoviesService
) {

    fun getMovies(queryType: MovieQueryType): Flowable<PagingData<MoviesModel>> = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { MoviesDataSource(moviesService, queryType) }
    ).flowable

    fun getAllMovieData(movieId: Int): Single<MovieInfoModel> {
        return Single.zip(
            moviesService.getMovieInfo(movieId),
            moviesService.getMoviesReviews(movieId),
            moviesService.getMovieCast(movieId),
            { data, reviews, cast ->
                MovieInfoModel(data, reviews, cast)
            }
        )
    }
}
