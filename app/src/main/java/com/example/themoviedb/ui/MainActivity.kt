package com.example.themoviedb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.themoviedb.R
import com.example.themoviedb.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.activity_fragment_container,
                    MoviesContainerFragment.newInstance()
                )
                .commit()
        }
        viewModel.isInternetAvailable.observe(this, {
            binding.noInternetMsg.isVisible = !it
        })
        Log.d("TAG", "onCreate: ")
    }
}
