package com.example.themoviedb.database

import androidx.room.TypeConverter
import com.example.themoviedb.models.movie_details.Genre
import com.example.themoviedb.models.movie_details.MovieCast
import com.example.themoviedb.models.movie_reviews.ReviewDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class MovieTypeConverters {

    @TypeConverter
    fun genresToString(genres: List<Genre>): String = Gson().toJson(genres)

    @TypeConverter
    fun stringToGenres(data: String?): List<Genre?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<Genre?>?>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun actorsToString(actors: MovieCast): String = Gson().toJson(actors)

    @TypeConverter
    fun stringToActors(string: String): MovieCast = Gson().fromJson(string, MovieCast::class.java)

    @TypeConverter
    fun reviewsToString(reviews: List<ReviewDetails>): String = Gson().toJson(reviews)

    @TypeConverter
    fun stringToReviews(data: String?): List<ReviewDetails?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<ReviewDetails?>?>() {}.type
        return Gson().fromJson(data, listType)
    }
}
