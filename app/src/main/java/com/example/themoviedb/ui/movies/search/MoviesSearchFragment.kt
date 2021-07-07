package com.example.themoviedb.ui.movies.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.example.themoviedb.R
import com.example.themoviedb.utils.LoadingStateAdapter
import com.example.themoviedb.ui.movies.MoviesPagingAdapter
import com.example.themoviedb.databinding.FragmentMoviesSearchBinding
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.ui.movie_details.MovieDetailsFragment
import com.example.themoviedb.ui.movies.MoviesViewModel
import com.example.themoviedb.repository.MovieQueryType
import com.jakewharton.rxbinding4.widget.textChanges
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MoviesSearchFragment : Fragment(), MoviesPagingAdapter.OnMovieClickListener,
    LoadingStateAdapter.OnRetryClickListener {

    private lateinit var binding: FragmentMoviesSearchBinding
    private val viewModel by viewModels<MoviesViewModel>()
    private val moviesRecyclerViewPagingAdapter: MoviesPagingAdapter by lazy {
        MoviesPagingAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMoviesSearchBinding.inflate(inflater, container, false)
        binding.moviesRecycler.adapter =
            moviesRecyclerViewPagingAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter(this)
            )
        searchMovie()

        setUpMovieSearch()
        return binding.root
    }

    private fun hideKeyboard() {
        (binding.searchView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            view?.windowToken,
            0
        )
    }

    private fun searchMovie() {
        viewModel.movies.observe(viewLifecycleOwner, {
            moviesRecyclerViewPagingAdapter.submitData(lifecycle, it)
            setUpProgressBar()
            checkIfMovieFound()
        })
    }

    private fun setUpMovieSearch() {

        binding.searchView.requestFocus()
        (binding.searchView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )

        binding.searchView.textChanges()
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(300, TimeUnit.MILLISECONDS)
            .filter { it.isNotEmpty() }
            .map { it.toString().trim() }
            .subscribe({
                viewModel.loadMovies(MovieQueryType.SearchString(it))
            }, {
                Log.d("TAG", "setUpMovieSearch: $it")
            })
    }

    private fun setUpProgressBar() {
        moviesRecyclerViewPagingAdapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        }
    }

    private fun checkIfMovieFound() {
        moviesRecyclerViewPagingAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.NotLoading) {
                binding.noMovieFoundImage.isVisible =
                    moviesRecyclerViewPagingAdapter.itemCount < 1
                binding.noMovieFoundText.isVisible =
                    moviesRecyclerViewPagingAdapter.itemCount < 1
            }
        }
    }

    override fun onMovieClick(
        movieModel: MoviesModel
    ) {
        hideKeyboard()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.activity_fragment_container,
                MovieDetailsFragment.newInstance(movieModel)
            ).addToBackStack(null)
            .commit()
    }

    override fun onRetryClick() {
        moviesRecyclerViewPagingAdapter.retry()
    }

    companion object {
        fun newInstance() = MoviesSearchFragment()
    }
}
