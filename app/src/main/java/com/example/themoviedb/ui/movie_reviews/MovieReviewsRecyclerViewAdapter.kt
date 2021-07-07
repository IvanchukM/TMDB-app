package com.example.themoviedb.ui.movie_reviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.R
import com.example.themoviedb.databinding.MovieReviewsItemBinding
import com.example.themoviedb.models.movie_reviews.ReviewDetails
import com.example.themoviedb.utils.extensions.loadImage
import com.example.themoviedb.utils.toReviewItem
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_TEMPLATE = "EEE, d MMM yyyy"

class MovieReviewsRecyclerViewAdapter :
    RecyclerView.Adapter<MovieReviewsViewHolder>() {

    private var reviewList: List<ReviewItem> = listOf()

    private lateinit var binding: MovieReviewsItemBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieReviewsViewHolder {
        binding = MovieReviewsItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieReviewsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MovieReviewsViewHolder,
        position: Int
    ) {
        holder.bind(item = reviewList[position])
    }

    override fun getItemCount(): Int {
        return reviewList.size
    }

    fun setReviews(reviewList: List<ReviewDetails>) {
        this.reviewList = reviewList.map { it.toReviewItem() }
        notifyDataSetChanged()
    }

    class ReviewItem(val modelReview: ReviewDetails, var avatarUrl: String?, var isExpanded: Boolean)
}

class MovieReviewsViewHolder(
    private val binding: MovieReviewsItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MovieReviewsRecyclerViewAdapter.ReviewItem) {

        if (item.avatarUrl != null) {
            binding.reviewerAvatar.loadImage(item.avatarUrl)
        } else {
            binding.reviewerAvatar.setImageResource(R.drawable.drawable_no_image_available)
        }

        val currentRating = item.modelReview.authorDetails?.rating
        if (currentRating != null) {
            binding.reviewerMovieRating.text = currentRating.toString()
            binding.movieRating.rating = (currentRating.div(2))
        } else {
            binding.movieRating.rating = 0f
            binding.reviewerMovieRating.text = ""
        }
        binding.movieRating.setIsIndicator(true)

        binding.reviewUpdateTime.text =
            SimpleDateFormat(
                DATE_TEMPLATE, Locale.ENGLISH
            ).format(item.modelReview.updatedAt)

        binding.movieReview.text = item.modelReview.content
        binding.reviewerName.text = item.modelReview.author

        hideExpandingArrow(item)
        setExpandableText(item, 0)

        binding.expandingArrow.setOnClickListener {
            item.isExpanded = !item.isExpanded
            setExpandableText(item, 300)
        }
    }

    private fun setExpandableText(item: MovieReviewsRecyclerViewAdapter.ReviewItem, animationDurationValue: Long) {
        binding.movieReview.setAnimationDuration(animationDurationValue)
        if (item.isExpanded) {
            binding.movieReview.expand()
            binding.expandingArrow.setBackgroundResource(R.drawable.ic_arrow_drop_up)
        } else {
            binding.movieReview.collapse()
            binding.expandingArrow.setBackgroundResource(R.drawable.ic_arrow_drop_down)
        }
    }

    private fun hideExpandingArrow(item: MovieReviewsRecyclerViewAdapter.ReviewItem) {
        binding.movieReview.post {
            if (binding.movieReview.layout.getEllipsisStart(binding.movieReview.layout.lineCount - 1) < 1) {
                binding.expandingArrow.visibility = View.GONE
            } else {
                binding.expandingArrow.visibility = View.VISIBLE
            }
            if (item.isExpanded) {
                binding.expandingArrow.visibility = View.VISIBLE
            }
        }
    }
}
