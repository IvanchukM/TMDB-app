package com.example.themoviedb.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.R
import com.example.themoviedb.databinding.FragmentFavoriteMoviesBinding
import com.example.themoviedb.models.MovieCommonDataModel
import com.example.themoviedb.ui.movie_details.MovieDetailsFragment
import com.example.themoviedb.utils.OnMovieClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMoviesFragment : Fragment(), OnMovieClickListener {

    private var _binding: FragmentFavoriteMoviesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FavoriteMoviesViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteMoviesBinding.inflate(inflater, container, false)

        val favoriteMoviesRecyclerViewAdapter = FavoriteMoviesRecyclerViewAdapter(this)
        binding.favoriteMoviesRecycler.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.favoriteMoviesRecycler.adapter = favoriteMoviesRecyclerViewAdapter

//        viewModel.favoriteMovies.observe(viewLifecycleOwner, {
//            favoriteMoviesRecyclerViewAdapter.setFavoriteMovies(it)
//        })
        return binding.root
    }

    companion object {
        fun newInstance() = FavoriteMoviesFragment()
    }

    override fun onMovieClick(movieCommonDataModel: MovieCommonDataModel) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.activity_fragment_container,
                MovieDetailsFragment.newInstance(movieCommonDataModel)
            )
            .addToBackStack(null)
            .commit()
    }
}
