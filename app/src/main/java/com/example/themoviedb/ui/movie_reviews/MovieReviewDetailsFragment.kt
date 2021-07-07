package com.example.themoviedb.ui.movie_reviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.databinding.FragmentMovieReviewDetailsBinding
import com.example.themoviedb.models.movie_reviews.ReviewDetails
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_MOVIE_MODEL = "movieModel"

@AndroidEntryPoint
class MovieReviewDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieReviewDetailsBinding

    private val viewModel by viewModels<ReviewDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val reviewDetails: List<ReviewDetails>? = arguments?.getParcelableArrayList<ReviewDetails>(ARG_MOVIE_MODEL)?.toList()
        if (savedInstanceState == null) {
            reviewDetails?.let {
                viewModel.setReviewDetails(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieReviewDetailsBinding.inflate(inflater, container, false)

        val reviewsRecyclerViewAdapter = MovieReviewsRecyclerViewAdapter()
        binding.recyclerViewReviews.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.recyclerViewReviews.adapter = reviewsRecyclerViewAdapter
        viewModel.movieReviews.observe(viewLifecycleOwner, { result ->
            reviewsRecyclerViewAdapter.setReviews(result)
        })
        return binding.root
    }

    companion object {
        fun newInstance(reviewDetails: List<ReviewDetails>) = MovieReviewDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(ARG_MOVIE_MODEL, ArrayList(reviewDetails))
            }
        }
    }
}
