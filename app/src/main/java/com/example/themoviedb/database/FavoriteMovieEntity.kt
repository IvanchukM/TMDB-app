package com.example.themoviedb.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.themoviedb.models.MovieInfoModel
import com.example.themoviedb.models.movie_details.Genre
import com.example.themoviedb.models.movie_details.MovieCast
import com.example.themoviedb.models.movie_reviews.ReviewDetails
import com.example.themoviedb.utils.extensions.convertIntoData
import com.example.themoviedb.utils.extensions.selectPosterPath


@Entity(tableName = FavoriteMovieEntity.TABLE_NAME)
data class FavoriteMovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
) {
    companion object {
        const val TABLE_NAME = "favourite_movies"
        const val MOVIE_ID = "movie_id"
    }
}

/**
 * Initial entity model for storing all necessary movie info
 */
/*
@Entity(tableName = FavoriteMovieEntity.TABLE_NAME)
data class FavoriteMovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "movie_id")
    val movieId: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "poster_path")
    val posterPath: String?,
    @ColumnInfo(name = "rating")
    val rating: Int?,
    @ColumnInfo(name = "votes")
    val votes: Int?,
    @ColumnInfo(name = "release_date")
    val releaseDate: String?,
    @ColumnInfo(name = "runtime")
    val runtime: Int?,
    @ColumnInfo(name = "genres")
    val genres: List<Genre>?,
    @ColumnInfo(name = "actors")
    val actors: MovieCast?,
    @ColumnInfo(name = "storyline")
    val storyline: String?,
    @ColumnInfo(name = "tag_line")
    val tagLine: String?,
    @ColumnInfo(name = "budget")
    val budget: Int?,
    @ColumnInfo(name = "revenue")
    val revenue: Int?,
    @ColumnInfo(name = "home_page")
    val homePage: String?,
    @ColumnInfo(name = "reviews")
    val reviews: List<ReviewDetails>?
) {
    companion object {
        const val TABLE_NAME = "favourite_movies"
    }
}

fun MovieInfoModel.toFavoriteEntity() = FavoriteMovieEntity(
    movieId = movieInfoResponse.id,
    title = movieInfoResponse.title,
    posterPath = selectPosterPath(),
    rating = movieInfoResponse.voteAverage,
    votes = movieInfoResponse.voteAverage,
    releaseDate = movieInfoResponse.releaseDate?.convertIntoData(),
    runtime = movieInfoResponse.runtime,
    genres = movieInfoResponse.genres,
    actors = movieCast,
    storyline = movieInfoResponse.overview,
    tagLine = movieInfoResponse.tagLine,
    budget = movieInfoResponse.budget,
    revenue = movieInfoResponse.revenue,
    homePage = movieInfoResponse.homepage,
    reviews = movieReviewsResponse.reviewDetails
)
*/