package com.example.themoviedb.utils

import com.example.themoviedb.BuildConfig
import com.example.themoviedb.models.movie_reviews.ReviewDetails
import com.example.themoviedb.ui.movie_reviews.MovieReviewsRecyclerViewAdapter

fun ReviewDetails.toReviewItem() = MovieReviewsRecyclerViewAdapter.ReviewItem(
    modelReview = ReviewDetails(
        author = author,
        authorDetails = authorDetails,
        content = content,
        createdAt = createdAt,
        id = id,
        updatedAt = updatedAt,
        url = url
    ),
    avatarUrl = getImageUrl(authorDetails?.avatarString),
    isExpanded = false
)

fun getImageUrl(url: String?): String {
    return if (url?.startsWith("/http") == true) {
        url.removePrefix("/")
    } else {
        BuildConfig.IMAGE_BASE_URL + url
    }
}
