package com.example.themoviedb.ui.movies.list

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themoviedb.R
import com.example.themoviedb.utils.LoadingStateAdapter
import com.example.themoviedb.ui.movies.MoviesPagingAdapter
import com.example.themoviedb.databinding.FragmentMoviesBinding
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.ui.movie_details.MovieDetailsFragment
import com.example.themoviedb.ui.movies.MoviesViewModel
import com.example.themoviedb.utils.ApplicationThemes
import com.example.themoviedb.utils.LayoutManagerTypes
import com.example.themoviedb.repository.MovieQueryType
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_MOVIE_STRING = "queryString"

@AndroidEntryPoint
class MoviesFragment : Fragment(), MoviesPagingAdapter.OnMovieClickListener,
        LoadingStateAdapter.OnRetryClickListener {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MoviesViewModel>()
    private val moviesRecyclerViewPagingAdapter: MoviesPagingAdapter by lazy {
        MoviesPagingAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            viewModel.loadMovies(
                    arguments?.getParcelable<MovieQueryType>(ARG_MOVIE_STRING) as MovieQueryType
            )
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMoviesBinding.inflate(inflater, container, false)

        binding.moviesListToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.app_theme_switch_button -> {
                    viewModel.updateApplicationTheme()
                }
                R.id.layout_switch_button -> {
                    viewModel.updateLayoutManagerType()
                }
            }
            true
        }

        binding.moviesRecycler.adapter =
                moviesRecyclerViewPagingAdapter.withLoadStateFooter(
                        footer = LoadingStateAdapter(this)
                )

        viewModel.movies.observe(viewLifecycleOwner, {
            moviesRecyclerViewPagingAdapter.submitData(lifecycle, it)
        })
        viewModel.currentMoviesSet.observe(viewLifecycleOwner, {
            binding.moviesListToolbar.title = it
        })
        viewModel.applicationTheme.observe(viewLifecycleOwner, { applicationTheme ->
            when (applicationTheme) {
                ApplicationThemes.LIGHT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.moviesListToolbar.menu.findItem(R.id.app_theme_switch_button)
                            .setIcon(R.drawable.ic_light_theme)
                }
                ApplicationThemes.DARK -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.moviesListToolbar.menu.findItem(R.id.app_theme_switch_button)
                            .setIcon(R.drawable.ic_dark_theme)
                }
                ApplicationThemes.AUTO -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    binding.moviesListToolbar.menu.findItem(R.id.app_theme_switch_button)
                            .setIcon(R.drawable.ic_auto_theme)
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    binding.moviesListToolbar.menu.findItem(R.id.app_theme_switch_button)
                            .setIcon(R.drawable.ic_auto_theme)
                }
            }
        })

        viewModel.layoutState.observe(viewLifecycleOwner, { recyclerViewState ->

            when (recyclerViewState) {
                LayoutManagerTypes.GRID -> {
                    binding.moviesRecycler.layoutManager = GridLayoutManager(activity, 2)
                    binding.moviesListToolbar.menu.findItem(R.id.layout_switch_button)
                            .setIcon(R.drawable.ic_grid_layout)
                }
                LayoutManagerTypes.LINEAR -> {
                    binding.moviesRecycler.layoutManager = LinearLayoutManager(activity)
                    binding.moviesListToolbar.menu.findItem(R.id.layout_switch_button)
                            .setIcon(R.drawable.ic_linear_layout)
                }
                else -> {
                    binding.moviesRecycler.layoutManager = LinearLayoutManager(activity)
                    binding.moviesListToolbar.menu.findItem(R.id.layout_switch_button)
                            .setIcon(R.drawable.ic_linear_layout)
                }
            }
        })

        setUpProgressBar()
        return binding.root
    }

    private fun setUpProgressBar() {
        moviesRecyclerViewPagingAdapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        }
    }

    override fun onMovieClick(
            movieModel: MoviesModel
    ) {
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(
                        R.id.activity_fragment_container,
                        MovieDetailsFragment.newInstance(movieModel)
                )
                .addToBackStack(null)
                .commit()
    }

    override fun onRetryClick() {
        moviesRecyclerViewPagingAdapter.retry()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(queryType: MovieQueryType) =
                MoviesFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_MOVIE_STRING, queryType)
                    }
                }
    }
}
