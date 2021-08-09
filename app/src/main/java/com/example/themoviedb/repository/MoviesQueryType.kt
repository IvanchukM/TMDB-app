package com.example.themoviedb.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class MovieQueryType : Parcelable {
    @Parcelize
    object Popular : MovieQueryType()

    @Parcelize
    object TopRated : MovieQueryType()

    @Parcelize
    object NowPlaying : MovieQueryType()

    @Parcelize
    class SearchString(val searchString: String) : MovieQueryType()
}
