package com.example.themoviedb.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.themoviedb.database.FavoriteMovieDao
import com.example.themoviedb.database.FavoriteMovieEntity
import com.example.themoviedb.models.MovieInfoModel
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.services.MoviesService
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val moviesService: MoviesService,
    private val favoriteMovieDao: FavoriteMovieDao
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

    fun getFavoriteMovies(): Single<List<FavoriteMovieEntity>> =
        favoriteMovieDao.getAllRecords()
            .subscribeOn(Schedulers.io())

    fun addFavoriteMovie(favoriteMovieEntity: FavoriteMovieEntity): Completable =
        favoriteMovieDao.addFavouriteMovie(favoriteMovieEntity)

    fun getFavoriteMovieDetailedInfo(movieId: Int): Single<FavoriteMovieEntity> =
        favoriteMovieDao.getFavoriteMovieDetailedInfo(movieId)

//    fun deleteFavoriteMovie(movieId: Int): Completable =
//        favoriteMovieDao.deleteFavoriteMovie(movieId)
//            .subscribeOn(Schedulers.io())
}
