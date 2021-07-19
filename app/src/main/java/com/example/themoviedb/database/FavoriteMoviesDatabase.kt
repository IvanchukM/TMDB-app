package com.example.themoviedb.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import javax.inject.Singleton

const val DB_NAME = "FavoriteMovies.db"
const val DB_VERSION = 7

@Singleton
@Database(
    entities = [FavoriteMovieEntity::class],
    version = DB_VERSION
)
@TypeConverters(MovieTypeConverters::class)
abstract class FavoriteMoviesDatabase : RoomDatabase() {
    abstract fun movieDao(): FavoriteMovieDao
}
