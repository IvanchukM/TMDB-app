package com.example.themoviedb.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.R
import com.example.themoviedb.databinding.MoviesItemBinding
import com.example.themoviedb.models.movies.MoviesModel
import com.example.themoviedb.models.toMovieCommonData
import com.example.themoviedb.utils.OnMovieClickListener
import com.example.themoviedb.utils.extensions.convertIntoYear
import com.example.themoviedb.utils.extensions.loadImageWithBaseUrl
import com.example.themoviedb.utils.extensions.selectPosterPath

class FavoriteMoviesRecyclerViewAdapter(private val onMovieClickListener: OnMovieClickListener) :
    RecyclerView.Adapter<FavoriteMoviesViewHolder>() {

    private var favoriteMoviesList: List<MoviesModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMoviesViewHolder {
        val binding = MoviesItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteMoviesViewHolder(binding, onMovieClickListener)
    }

    override fun onBindViewHolder(holder: FavoriteMoviesViewHolder, position: Int) {
        holder.bind(favoriteMoviesList[position])
    }

    override fun getItemCount(): Int {
        return favoriteMoviesList.size
    }

    fun setFavoriteMovies(favoriteMoviesList: List<MoviesModel>) {
        this.favoriteMoviesList = favoriteMoviesList
        notifyDataSetChanged()
    }

}

class FavoriteMoviesViewHolder(
    private val binding: MoviesItemBinding,
    private val onMovieClickListener: OnMovieClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movieModel: MoviesModel) {
        if (movieModel.selectPosterPath() != null) {
            binding.moviePoster.loadImageWithBaseUrl(movieModel.selectPosterPath())
        } else {
            binding.moviePoster.setImageResource(R.drawable.drawable_no_image_available)
        }
        binding.movieTitle.text = movieModel.title
        binding.movieReleaseDate.text = movieModel.releaseDate?.convertIntoYear()
        binding.movieCard.setOnClickListener {
            onMovieClickListener.onMovieClick(
                movieModel.toMovieCommonData()
            )
        }
    }
}

