package com.example.themoviedb.ui.movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.R
import com.example.themoviedb.databinding.MoviesItemBinding
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.models.toMovieCommonData
import com.example.themoviedb.utils.OnMovieClickListener
import com.example.themoviedb.utils.extensions.convertIntoData
import com.example.themoviedb.utils.extensions.loadImageWithBaseUrl
import com.example.themoviedb.utils.extensions.selectPosterPath

class MoviesPagingAdapter(private val onMovieClickListener: OnMovieClickListener) :
    PagingDataAdapter<MoviesModel, MoviesViewHolder>(diffUtilComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val binding = MoviesItemBinding
            .inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        return MoviesViewHolder(binding, onMovieClickListener)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        val diffUtilComparator = object : DiffUtil.ItemCallback<MoviesModel>() {
            override fun areItemsTheSame(
                oldItem: MoviesModel,
                newItem: MoviesModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MoviesModel,
                newItem: MoviesModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class MoviesViewHolder(
    private val binding: MoviesItemBinding,
    private val onMovieClickListener: OnMovieClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movieModel: MoviesModel) {
        if (movieModel.backdropPath != null) {
            binding.moviePoster.loadImageWithBaseUrl(movieModel.selectPosterPath())
        } else {
            binding.moviePoster.setImageResource(R.drawable.drawable_no_image_available)
        }
        binding.movieTitle.text = movieModel.title
        binding.movieReleaseDate.text = movieModel.releaseDate?.convertIntoData()
        binding.movieCard.setOnClickListener {
            onMovieClickListener.onMovieClick(
                movieModel.toMovieCommonData()
            )
        }
    }
}
