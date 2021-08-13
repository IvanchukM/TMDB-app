package com.example.themoviedb.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class AccountQueryType : Parcelable {
    @Parcelize
    object Favorite : AccountQueryType()

    @Parcelize
    object Rated : AccountQueryType()

    @Parcelize
    object Watchlist : AccountQueryType()
}
