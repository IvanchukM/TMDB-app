package com.example.themoviedb.ui.movie_details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.R
import com.example.themoviedb.databinding.FragmentMovieDetailsBinding
import com.example.themoviedb.models.account_movies.ToggleFavoriteMovieStateModel
import com.example.themoviedb.models.account_movies.ToggleWatchlistStateModel
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
private const val RATING_DIALOG_FRAGMENT = "rateFragment"

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(), OnMovieRated {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<MovieDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieInfo = arguments?.getParcelable<MoviesModel>(ARG_MOVIE_MODEL)
        if (savedInstanceState == null) {
            movieInfo?.let {
                viewModel.setMovieData(it)
                viewModel.getMovieState(it.id)
            }
        }
        postponeEnterTransition()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        binding.recyclerViewActors.isNestedScrollingEnabled = false

        val actorsRecyclerAdapter = ActorsRecyclerViewAdapter()

        binding.movieDetailsToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

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
            binding.movieDetailsToolbar.title = it.title
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

        binding.movieReviewsSeeMore.setOnClickListener {
            openReviewDetails()
        }
        binding.movieDetailsToolbar.setOnMenuItemClickListener { item ->
            if (viewModel.isUserLoginIn.value == true) {
                when (item.itemId) {
                    R.id.toggle_favorite -> {
                        switchFavoriteMovieState(
                            viewModel.movieState.value?.favorite
                        )
                    }
                    R.id.toggle_rating -> {
                        RateMovieDialogFragment(this).show(
                            parentFragmentManager,
                            RATING_DIALOG_FRAGMENT
                        )
                    }
                    R.id.toggle_watchlist -> {
                        switchWatchlistState(
                            viewModel.movieState.value?.watchlist
                        )
                    }
                }
            } else {
                Toast.makeText(activity, getString(R.string.ask_for_login), Toast.LENGTH_SHORT)
                    .show()
            }
            true
        }

        viewModel.movieState.observe(viewLifecycleOwner, { movieState ->
            if (movieState.favorite) {
                binding.movieDetailsToolbar.menu.findItem(R.id.toggle_favorite).title =
                    getString(R.string.remove_from_favorite)
            } else {
                binding.movieDetailsToolbar.menu.findItem(R.id.toggle_favorite).title =
                    getString(R.string.add_to_favorite)
            }
            if (movieState.movieRating.isMovieRated == false) {
                binding.movieDetailsToolbar.menu.findItem(R.id.toggle_rating).title =
                    getString(R.string.rate_movie)
            } else {
                binding.movieDetailsToolbar.menu.findItem(R.id.toggle_rating).title =
                    getString(R.string.change_movie_rating)
                showUsersMovieRating(movieState.movieRating.rating.toString())
            }
            if (movieState.watchlist) {
                binding.movieDetailsToolbar.menu.findItem(R.id.toggle_watchlist).title =
                    getString(R.string.remove_from_watchlist)
            } else {
                binding.movieDetailsToolbar.menu.findItem(R.id.toggle_watchlist).title =
                    getString(R.string.add_to_watchlist)
            }
        })

        viewModel.testToast.observe(viewLifecycleOwner, { toastMsg ->
            Toast.makeText(activity, toastMsg, Toast.LENGTH_SHORT).show()
        })
        return binding.root
    }

    private fun showUsersMovieRating(movieRating: String) {
        binding.userMovieRating.isVisible = true
        binding.userMovieRating.text = movieRating
    }

    private fun switchFavoriteMovieState(movieFavoriteState: Boolean?) {
        val movieId = arguments?.getParcelable<MoviesModel>(ARG_MOVIE_MODEL)?.id
        if (movieId != null && movieFavoriteState != null) {
            viewModel.switchMovieFavoriteState(
                ToggleFavoriteMovieStateModel(
                    movieId = movieId,
                    isFavorite = !movieFavoriteState
                )
            )
        }
    }

    private fun switchWatchlistState(watchlistState: Boolean?) {
        val movieId = arguments?.getParcelable<MoviesModel>(ARG_MOVIE_MODEL)?.id
        if (movieId != null && watchlistState != null) {
            viewModel.switchWatchlistState(
                ToggleWatchlistStateModel(
                    movieId = movieId,
                    isInWatchlist = !watchlistState
                )
            )
        }
    }

    override fun rateMovie(movieRating: Float) {
        val movieId = arguments?.getParcelable<MoviesModel>(ARG_MOVIE_MODEL)?.id
        if (movieId != null) {
            viewModel.setMovieRating(movieId, movieRating)
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
