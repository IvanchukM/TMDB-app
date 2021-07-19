package com.example.themoviedb.models

import android.os.Parcelable
import com.example.themoviedb.database.FavoriteMovieEntity
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.utils.extensions.convertIntoYear
import com.example.themoviedb.utils.extensions.selectPosterPath
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieCommonDataModel(
    val movieId: Int,
    val title: String,
    val posterPath: String?,
    val rating: Float?,
    val votes: Float,
    val releaseDate: String?
) : Parcelable

fun MoviesModel.toMovieCommonData() = MovieCommonDataModel(
    movieId = id,
    title = title,
    posterPath = selectPosterPath(),
    rating = voteAverage,
    votes = voteCount.toFloat(),
    releaseDate = releaseDate?.convertIntoYear()
)

