package com.example.themoviedb.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface FavoriteMovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavouriteMovie(favoriteMovieInfo: FavoriteMovieEntity): Completable

    @Query("SELECT * FROM ${FavoriteMovieEntity.TABLE_NAME}")
    fun getAllRecords(): Single<List<FavoriteMovieEntity>>

    @Query("SELECT * FROM ${FavoriteMovieEntity.TABLE_NAME} WHERE ${FavoriteMovieEntity.MOVIE_ID} = :movieId")
    fun getFavoriteMovieDetailedInfo(movieId: Int): Single<FavoriteMovieEntity>

//    @Delete
//    fun deleteFavoriteMovie(movieId: Int): Completable
}