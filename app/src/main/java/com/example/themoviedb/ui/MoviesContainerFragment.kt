package com.example.themoviedb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.themoviedb.R
import com.example.themoviedb.databinding.FragmentMoviesContainerBinding
import com.example.themoviedb.ui.movies.list.MoviesFragment
import com.example.themoviedb.ui.movies.search.MoviesSearchFragment
import com.example.themoviedb.repository.MovieQueryType

class MoviesContainerFragment : Fragment() {

    private lateinit var binding: FragmentMoviesContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            loadMovieFragment(MovieQueryType.NowPlaying)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesContainerBinding.inflate(inflater, container, false)

        setUpNavMenu()

        binding.fab.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(
                    R.id.activity_fragment_container,
                    MoviesSearchFragment.newInstance()
                )
                .addToBackStack(null)
                .commit()
        }
        return binding.root
    }

    private fun setUpNavMenu() {
        binding.bottomNavigationMenu.setOnNavigationItemReselectedListener {}
        binding.bottomNavigationMenu.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.now_playing_movies_fragment -> {
                    loadMovieFragment(MovieQueryType.NowPlaying)
                }
                R.id.popular_movies_fragment -> {
                    loadMovieFragment(MovieQueryType.Popular)
                }
                R.id.top_rated_movies_fragment -> {
                    loadMovieFragment(MovieQueryType.TopRated)
                }
            }
            true
        }
    }

    private fun loadMovieFragment(queryType: MovieQueryType) {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, MoviesFragment.newInstance(queryType))
            .commit()
    }

    companion object {
        fun newInstance() = MoviesContainerFragment()
    }
}
