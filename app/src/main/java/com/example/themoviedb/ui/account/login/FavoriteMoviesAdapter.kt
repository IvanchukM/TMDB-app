package com.example.themoviedb.ui.account.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.R
import com.example.themoviedb.databinding.MoviesItemBinding
import com.example.themoviedb.models.account_movies.AccountMovieModel
import com.example.themoviedb.utils.extensions.convertIntoData
import com.example.themoviedb.utils.extensions.loadImageWithBaseUrl
import com.example.themoviedb.utils.extensions.selectPosterPath

class FavoriteMoviesAdapter() :
    PagingDataAdapter<AccountMovieModel, FavoriteMoviesViewHolder>(diffUtilComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMoviesViewHolder {
        val binding = MoviesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteMoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteMoviesViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        val diffUtilComparator = object : DiffUtil.ItemCallback<AccountMovieModel>() {
            override fun areItemsTheSame(
                oldItem: AccountMovieModel,
                newItem: AccountMovieModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AccountMovieModel,
                newItem: AccountMovieModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class FavoriteMoviesViewHolder(private val binding: MoviesItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(accountMovies: AccountMovieModel) {
        if (accountMovies.backdropPath != null) {
            binding.moviePoster.loadImageWithBaseUrl(accountMovies.selectPosterPath())
        } else {
            binding.moviePoster.setImageResource(R.drawable.drawable_no_image_available)
        }
        binding.movieTitle.text = accountMovies.title
        binding.movieReleaseDate.text = accountMovies.releaseDate?.convertIntoData()
    }
}
