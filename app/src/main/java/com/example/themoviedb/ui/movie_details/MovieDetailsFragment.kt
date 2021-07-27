package com.example.themoviedb.ui.movie_details

import android.graphics.Color
import android.os.Bundle
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

private const val ARG_MOVIE_MODEL = "movieModel"

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailsBinding

    private val viewModel by viewModels<MovieDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieInfo = arguments?.getParcelable<MoviesModel>(ARG_MOVIE_MODEL)
        if (savedInstanceState == null) {
            movieInfo?.let {
                viewModel.setMovieData(it)
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
            binding.movieImage.loadImageWithBaseUrl(it.selectPosterPath()) {
                startPostponedEnterTransition()
            }
            binding.movieYear.text = it.releaseDate?.convertIntoYear()
            binding.averageRating.text = it.voteAverage.toString()
            binding.movieRating.rating = it.voteAverage.div(2)
            binding.movieRating.setIsIndicator(true)
            binding.totalVotes.text =
                resources.getString(R.string.movie_votes, it.voteCount)
            binding.myToolbar.title = it.title
            binding.movieRating.visibility = View.VISIBLE
            binding.movieYear.visibility = View.VISIBLE
        })

        viewModel.movieData.observe(viewLifecycleOwner, {
            if (it.releaseDate == null) {
                binding.movieYear.visibility = View.GONE
            }
            binding.movieRuntime.text =
                resources.getString(R.string.movie_runtime, it.runtime / 60, it.runtime % 60)
            addMovieGenreChip(it.genres, binding.movieGenres)
            binding.movieStoryline.text = it.overview
            binding.movieTagLineText.text = it.tagLine
            binding.movieBudgetText.text = resources.getString(R.string.movie_budget, it.budget)
            binding.movieRevenueText.text =
                resources.getString(R.string.movie_budget, it.revenue)
            binding.premiereText.text = it.releaseDate?.convertIntoData()
            binding.homePageLink.text = it.homepage
            fillEmptyFields(
                it.tagLine, it.budget, it.revenue,
                it.releaseDate?.convertIntoData(), it.homepage,
                it.overview
            )
        })

        viewModel.movieActorsData.observe(viewLifecycleOwner, {
            if (it.actors.isNullOrEmpty()) {
                binding.noActors.visibility = View.VISIBLE
                binding.recyclerViewActors.visibility = View.GONE
            }
            actorsRecyclerAdapter.setActors(it.actors)
        })

        viewModel.movieReviews.observe(viewLifecycleOwner, { reviews ->
            if (reviews.isNotEmpty()) {
                binding.movieFirstReview.text = reviews.first().content
            } else {
                binding.movieFirstReview.visibility = View.GONE
                binding.movieReviewsSeeMore.visibility = View.GONE
                binding.noMovieReview.visibility = View.VISIBLE
            }
        })

        binding.addToFavorites.setOnClickListener {
            val movieId = arguments?.getParcelable<MoviesModel>(ARG_MOVIE_MODEL)?.id
            viewModel.checkIfUserLoginIn()
            viewModel.isUserLoginIn.observe(viewLifecycleOwner, { userLoginIn ->
                if (userLoginIn) {
                    movieId?.let { id -> viewModel.addFavoriteMovie(id) }
                } else {
                    Toast.makeText(
                        activity,
                        resources.getString(R.string.require_login_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }

        binding.movieReviewsSeeMore.setOnClickListener {
            openReviewDetails()
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
        viewModel.movieReviews.value?.let {
            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.activity_fragment_container, MovieReviewDetailsFragment.newInstance(it)
                )
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        fun newInstance(
            moviesModel: MoviesModel
        ) = MovieDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_MOVIE_MODEL, moviesModel)
            }
        }
    }
}
