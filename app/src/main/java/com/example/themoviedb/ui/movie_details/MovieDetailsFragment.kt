package com.example.themoviedb.ui.movie_details

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.R
import com.example.themoviedb.databinding.FragmentMovieDetailsBinding
import com.example.themoviedb.models.MovieCommonDataModel
import com.example.themoviedb.models.MovieDetailsModel
import com.example.themoviedb.models.movie_details.Genre
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.ui.movie_reviews.MovieReviewDetailsFragment
import com.example.themoviedb.utils.LoadingState
import com.example.themoviedb.utils.extensions.convertIntoData
import com.example.themoviedb.utils.extensions.convertIntoYear
import com.example.themoviedb.utils.extensions.loadImageWithBaseUrl
import com.example.themoviedb.utils.extensions.selectPosterPath
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_MOVIE_MODEL = "movieCommonData"

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding

    private val viewModel by viewModels<MovieDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieInfo = arguments?.getParcelable<MovieCommonDataModel>(ARG_MOVIE_MODEL)
        if (savedInstanceState == null) {
            if (movieInfo != null) {
                viewModel.setMovieData(movieInfo)
            }
        }
        postponeEnterTransition()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        binding.recyclerViewActors.isNestedScrollingEnabled = false

        val actorsRecyclerAdapter = ActorsRecyclerViewAdapter()

        binding.recyclerViewActors.layoutManager =
            LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)

        binding.recyclerViewActors.adapter = actorsRecyclerAdapter

        viewModel.loadingState.observe(viewLifecycleOwner, {
            when (it) {
                is LoadingState.Load -> showLoadBar()
                is LoadingState.Success -> hideLoadBar()
                is LoadingState.Error -> {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.error_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        viewModel.movieCommonData.observe(viewLifecycleOwner, {
            binding.movieImage.loadImageWithBaseUrl(it.posterPath) {
                startPostponedEnterTransition()
            }
            binding.movieYear.text = it.releaseDate
            binding.averageRating.text = it.votes.toString()
            binding.movieRating.rating = it.votes.div(2)
            binding.movieRating.setIsIndicator(true)
            binding.totalVotes.text =
                resources.getString(R.string.movie_votes, it.votes.toInt())
            binding.myToolbar.title = it.title
            binding.movieRating.visibility = View.VISIBLE
            binding.movieYear.visibility = View.VISIBLE
        })

        viewModel.movieDetails.observe(viewLifecycleOwner, {
            if (it.releaseDate == null) {
                binding.movieYear.visibility = View.GONE
            }
            binding.movieRuntime.text =
                resources.getString(
                    R.string.movie_runtime, it.runtime?.div(60),
                    it.runtime?.rem(60)
                )
            it.genres?.let { it1 -> addMovieGenreChip(it1, binding.movieGenres) }
            binding.movieStoryline.text = it.storyline
            binding.movieTagLineText.text = it.tagLine
            binding.movieBudgetText.text = resources.getString(R.string.movie_budget, it.budget)
            binding.movieRevenueText.text =
                resources.getString(R.string.movie_budget, it.revenue)
            binding.premiereText.text = it.releaseDate
            binding.homePageLink.text = it.homePage
            fillEmptyFields(
                it.tagLine, it.budget, it.revenue,
                it.releaseDate, it.homePage,
                it.storyline
            )
        })

        viewModel.movieDetails.observe(viewLifecycleOwner, {
            if (it.actors?.actors.isNullOrEmpty()) {
                binding.noActors.visibility = View.VISIBLE
                binding.recyclerViewActors.visibility = View.GONE
            }
            it.actors?.let { actorsInfo -> actorsRecyclerAdapter.setActors(actorsInfo.actors) }
        })

        viewModel.movieDetails.observe(viewLifecycleOwner, { reviews ->
            if (reviews.reviews?.isNotEmpty() == true) {
                binding.movieFirstReview.text = reviews.reviews.first().content
            } else {
                binding.movieFirstReview.visibility = View.GONE
                binding.movieReviewsSeeMore.visibility = View.GONE
                binding.noMovieReview.visibility = View.VISIBLE
            }
        })

        binding.movieReviewsSeeMore.setOnClickListener {
            openReviewDetails()
        }

        binding.addToFavorites.setOnClickListener {
//            addToFavorite()
        }

        return binding.root
    }

    private fun fillEmptyFields(
        tagLine: String?,
        budget: Int?,
        revenue: Int?,
        premiere: String?,
        homePage: String?,
        storyline: String?
    ) {
        if (tagLine.isNullOrEmpty()) binding.movieTagLineText.text =
            resources.getString(R.string.no_info_filler)
        if (budget == 0) binding.movieBudgetText.text =
            resources.getString(R.string.no_info_filler)
        if (revenue == 0) binding.movieRevenueText.text =
            resources.getString(R.string.no_info_filler)
        if (premiere.isNullOrEmpty()) binding.premiereText.text =
            resources.getString(R.string.no_info_filler)
        if (homePage.isNullOrEmpty()) binding.homePageLink.text =
            resources.getString(R.string.no_info_filler)
        if (storyline.isNullOrEmpty()) binding.movieStoryline.text =
            resources.getString(R.string.no_info_filler)
    }

    private fun addMovieGenreChip(genres: List<Genre>, chipGroup: ChipGroup) {
        genres.map {
            Chip(context).apply {
                setTextColor(Color.WHITE)
                text = it.name
                isClickable = false
                setChipBackgroundColorResource(R.color.yellow_700)
            }
        }.forEach {
            chipGroup.addView(it)
        }
    }

    private fun showLoadBar() {
        binding.nestedScrollView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadBar() {
        binding.nestedScrollView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun openReviewDetails() {
        val reviews = viewModel.movieDetails.value?.reviews
        if (reviews != null) {
            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.activity_fragment_container,
                    MovieReviewDetailsFragment.newInstance(reviews)
                )
                .addToBackStack(null)
                .commit()
        }
    }

//    private fun addToFavorite() {
//        viewModel.addFavoriteMovie()
//    }

    companion object {
        fun newInstance(
            movieCommonDataModel: MovieCommonDataModel
        ) = MovieDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_MOVIE_MODEL, movieCommonDataModel)
            }
        }
    }
}
