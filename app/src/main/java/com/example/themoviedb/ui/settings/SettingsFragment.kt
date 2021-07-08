package com.example.themoviedb.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.themoviedb.R
import com.example.themoviedb.databinding.FragmentSettingsBinding
import com.example.themoviedb.utils.ApplicationThemes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.darkThemeRadioButton.id -> viewModel.updateApplicationTheme(
                    ApplicationThemes.DARK
                )
                binding.lightThemeRadioButton.id -> viewModel.updateApplicationTheme(
                    ApplicationThemes.LIGHT
                )
                binding.autoThemeRadioButton.id -> viewModel.updateApplicationTheme(
                    ApplicationThemes.AUTO
                )
            }
        }

        viewModel.applicationTheme.observe(viewLifecycleOwner, { applicationTheme ->
            when (applicationTheme) {
                ApplicationThemes.LIGHT -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.lightThemeRadioButton.isChecked = true
                }
                ApplicationThemes.DARK -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.darkThemeRadioButton.isChecked = true
                }
                ApplicationThemes.AUTO -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    binding.autoThemeRadioButton.isChecked = true
                }
                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    binding.autoThemeRadioButton.isChecked = true
                }
            }
        })

        return binding.root
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}
