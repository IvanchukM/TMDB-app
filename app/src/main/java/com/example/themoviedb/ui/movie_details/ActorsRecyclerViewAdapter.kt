package com.example.themoviedb.ui.movie_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.R
import com.example.themoviedb.databinding.MovieActorsItemBinding
import com.example.themoviedb.models.movie_details.MovieActors
import com.example.themoviedb.utils.extensions.loadImageWithBaseUrl

class ActorsRecyclerViewAdapter : RecyclerView.Adapter<ActorsViewHolder>() {

    private var actorsList: List<MovieActors> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {

        val binding = MovieActorsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ActorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        holder.bind(actorsList[position])
    }

    override fun getItemCount(): Int {
        return actorsList.size
    }

    fun setActors(actorsList: List<MovieActors>) {
        this.actorsList = actorsList
        notifyDataSetChanged()
    }
}

class ActorsViewHolder(private val binding: MovieActorsItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(movieActors: MovieActors) {
        if (movieActors.profilePath != null) {
            binding.actorPhoto.loadImageWithBaseUrl(movieActors.profilePath)
        } else {
            binding.actorPhoto.setImageResource(R.drawable.drawable_no_image_available)
        }
        binding.actorName.text = movieActors.name
    }
}
