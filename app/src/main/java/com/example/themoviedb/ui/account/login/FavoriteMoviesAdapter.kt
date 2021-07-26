package com.example.themoviedb.ui.account.login

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.R
import com.example.themoviedb.databinding.MoviesItemBinding
import com.example.themoviedb.models.favorite_movies.FavoriteMovieModel
import com.example.themoviedb.utils.extensions.convertIntoData
import com.example.themoviedb.utils.extensions.loadImageWithBaseUrl
import com.example.themoviedb.utils.extensions.selectPosterPath

class FavoriteMoviesAdapter() :
    PagingDataAdapter<FavoriteMovieModel, FavoriteMoviesViewHolder>(diffUtilComparator) {

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
        val diffUtilComparator = object : DiffUtil.ItemCallback<FavoriteMovieModel>() {
            override fun areItemsTheSame(
                oldItem: FavoriteMovieModel,
                newItem: FavoriteMovieModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FavoriteMovieModel,
                newItem: FavoriteMovieModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class FavoriteMoviesViewHolder(private val binding: MoviesItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(favoriteMovies: FavoriteMovieModel) {
        if (favoriteMovies.backdropPath != null) {
            binding.moviePoster.loadImageWithBaseUrl(favoriteMovies.selectPosterPath())
        } else {
            binding.moviePoster.setImageResource(R.drawable.drawable_no_image_available)
        }
        binding.movieTitle.text = favoriteMovies.title
        binding.movieReleaseDate.text = favoriteMovies.releaseDate?.convertIntoData()
    }
}
