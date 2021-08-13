package com.example.themoviedb.ui.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.themoviedb.databinding.FragmentRateMovieBinding

class RateMovieDialogFragment(private val onMovieRated: OnMovieRated) : DialogFragment() {
    private var _binding: FragmentRateMovieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRateMovieBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.submitButton.setOnClickListener {
            onMovieRated.rateMovie(binding.ratingBar.rating)
            dismiss()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

interface OnMovieRated {
    fun rateMovie(movieRating: Float)
}