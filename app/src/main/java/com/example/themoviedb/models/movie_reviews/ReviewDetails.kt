package com.example.themoviedb.models.movie_reviews

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

@Parcelize
data class ReviewDetails(
    val author: String?,
    @SerializedName("author_details")
    val authorDetails: @RawValue AuthorDetails?,
    val content: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    val id: String?,
    @SerializedName("updated_at")
    val updatedAt: Date,
    val url: String?
) : Parcelable
