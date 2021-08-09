package com.example.themoviedb.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.themoviedb.R
import com.example.themoviedb.databinding.FragmentAccountContainerBinding
import com.example.themoviedb.repository.AccountQueryType
import com.example.themoviedb.repository.MovieQueryType

class AccountContainerFragment : Fragment() {

    private var _binding: FragmentAccountContainerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            loadAccountFragment(AccountQueryType.Favorite)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountContainerBinding.inflate(inflater, container, false)

        setUpNavMenu()
        return binding.root
    }

    private fun setUpNavMenu() {
        binding.bottomNavigationMenu.setOnNavigationItemReselectedListener {}
        binding.bottomNavigationMenu.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.favorite_movies -> {
                    loadAccountFragment(AccountQueryType.Favorite)
                }
                R.id.rated_movies -> {
                    loadAccountFragment(AccountQueryType.Rated)
                }
                R.id.watchlist -> {
                    loadAccountFragment(AccountQueryType.Watchlist)
                }
            }
            true
        }
    }

    private fun loadAccountFragment(queryType: AccountQueryType) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.account_fragment_container, UserAccountFragment.newInstance(queryType))
            .commit()
    }

    companion object {
        fun newInstance() = AccountContainerFragment()
    }
}