package com.example.themoviedb.ui.movie_reviews

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themoviedb.models.movie_reviews.ReviewDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewDetailsViewModel @Inject constructor() : ViewModel() {

    val movieReviews = MutableLiveData<List<ReviewDetails>>()

    fun setReviewDetails(reviewDetails: List<ReviewDetails>) {
        movieReviews.postValue(reviewDetails)
    }
}
