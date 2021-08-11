package com.example.themoviedb.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.R
import com.example.themoviedb.databinding.FragmentUserAccountBinding
import com.example.themoviedb.repository.AccountQueryType
import com.example.themoviedb.ui.account.login.FavoriteMoviesAdapter
import com.example.themoviedb.ui.account.login.LoginFragment
import com.example.themoviedb.utils.LoadingStateAdapter
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_QUERY_STRING = "queryString"

@AndroidEntryPoint
class UserAccountFragment : Fragment(), LoadingStateAdapter.OnRetryClickListener {
    private var _binding: FragmentUserAccountBinding? = null
    val binding get() = _binding!!
    private val viewModel by viewModels<UserAccountViewModel>()
    private val favoritesMoviesAdapter: FavoriteMoviesAdapter by lazy {
        FavoriteMoviesAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.loadAccountMovies(
                arguments?.getParcelable<AccountQueryType>(ARG_QUERY_STRING) as AccountQueryType
            )
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAccountBinding.inflate(inflater, container, false)

        binding.accountToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.logout -> {
                    viewModel.logoutUser()
                    openLoginFragment()
                }
            }
            true
        }
        binding.favoriteMoviesRecycler.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        binding.favoriteMoviesRecycler.adapter = favoritesMoviesAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter(this)
        )
        viewModel.movieData.observe(viewLifecycleOwner, {
            favoritesMoviesAdapter.submitData(lifecycle, it)
        })
        viewModel.currentQuery.observe(viewLifecycleOwner, { query ->
            when (query) {
                AccountQueryType.Favorite -> binding.accountToolbar.title =
                    resources.getString(R.string.favorite)
                AccountQueryType.Rated -> binding.accountToolbar.title =
                    resources.getString(R.string.rated)
                AccountQueryType.Watchlist -> binding.accountToolbar.title =
                    resources.getString(R.string.watchlist)
            }
        })
        setUpProgressBar()

        return binding.root
    }

    private fun openLoginFragment() {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.activity_fragment_container,
                LoginFragment.newInstance()
            )
            .addToBackStack(null)
            .commit()
    }

    private fun setUpProgressBar() {
        favoritesMoviesAdapter.addLoadStateListener { loadState ->
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onRetryClick() {
        favoritesMoviesAdapter.retry()
    }

    companion object {
        fun newInstance(queryType: AccountQueryType) = UserAccountFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_QUERY_STRING, queryType)
            }
        }
    }
}
