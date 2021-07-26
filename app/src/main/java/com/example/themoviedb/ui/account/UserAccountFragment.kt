package com.example.themoviedb.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.R
import com.example.themoviedb.databinding.FragmentUserAccountBinding
import com.example.themoviedb.ui.account.login.FavoriteMoviesAdapter
import com.example.themoviedb.ui.account.login.LoginFragment
import com.example.themoviedb.ui.movies.MoviesPagingAdapter
import com.example.themoviedb.utils.LoadingStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserAccountFragment : Fragment(), LoadingStateAdapter.OnRetryClickListener {
    private var _binding: FragmentUserAccountBinding? = null
    val binding get() = _binding!!
    private val viewModel by viewModels<UserAccountViewModel>()
    private val favoritesMoviesAdapter: FavoriteMoviesAdapter by lazy {
        FavoriteMoviesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAccountBinding.inflate(inflater, container, false)

        binding.goToLoginBtn.setOnClickListener {
            openLoginFragment()
        }
        binding.getFavMoviesBtn.setOnClickListener {
            getFavMovies()
        }
        binding.favoriteMoviesRecycler.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.favoriteMoviesRecycler.adapter = favoritesMoviesAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter(this)
        )
        viewModel.favoriteMovies.observe(viewLifecycleOwner, {
            favoritesMoviesAdapter.submitData(lifecycle, it)
        })
        return binding.root
    }

    private fun getFavMovies() {
        viewModel.getFavoriteMovies()
    }

    private fun openLoginFragment() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.activity_fragment_container,
                LoginFragment.newInstance()
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onRetryClick() {
        favoritesMoviesAdapter.retry()
    }

    companion object {
        fun newInstance() = UserAccountFragment()
    }
}
