package com.example.themoviedb.ui.movies.list

import android.os.Bundle
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
import com.example.themoviedb.databinding.FragmentMoviesBinding
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.repository.MovieQueryType
import com.example.themoviedb.ui.account.AccountContainerFragment
import com.example.themoviedb.ui.account.login.LoginFragment
import com.example.themoviedb.ui.movie_details.MovieDetailsFragment
import com.example.themoviedb.ui.movies.MoviesPagingAdapter
import com.example.themoviedb.ui.movies.MoviesViewModel
import com.example.themoviedb.ui.settings.SettingsFragment
import com.example.themoviedb.utils.LayoutManagerTypes
import com.example.themoviedb.utils.LoadingStateAdapter
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

        binding.movieListToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    viewModel.checkIfUserLoginIn()
                    checkIfUserLoginIn()
                }
                R.id.layout_switch_button -> {
                    viewModel.updateLayoutManagerType()
                }
                R.id.app_settings -> {
                    openSettingsFragment()
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

        viewModel.layoutState.observe(viewLifecycleOwner, { recyclerViewState ->

            when (recyclerViewState) {
                LayoutManagerTypes.GRID -> {
                    binding.moviesRecycler.layoutManager = GridLayoutManager(activity, 2)
                    binding.movieListToolbar.menu.findItem(R.id.layout_switch_button)
                        .setIcon(R.drawable.ic_grid_layout)
                }
                LayoutManagerTypes.LINEAR -> {
                    binding.moviesRecycler.layoutManager = LinearLayoutManager(activity)
                    binding.movieListToolbar.menu.findItem(R.id.layout_switch_button)
                        .setIcon(R.drawable.ic_linear_layout)
                }
                else -> {
                    binding.moviesRecycler.layoutManager = LinearLayoutManager(activity)
                    binding.movieListToolbar.menu.findItem(R.id.layout_switch_button)
                        .setIcon(R.drawable.ic_linear_layout)
                }
            }
        })

        setUpProgressBar()
        return binding.root
    }

    private fun checkIfUserLoginIn() {
        val fragment = if (viewModel.isUserLoginIn.value == true) {
            LoginFragment.newInstance()
        } else {
            AccountContainerFragment.newInstance()
        }
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.activity_fragment_container,
                fragment
            )
            .addToBackStack(null)
            .commit()
    }


    private fun openSettingsFragment() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.activity_fragment_container,
                SettingsFragment.newInstance()
            )
            .addToBackStack(null)
            .commit()
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
