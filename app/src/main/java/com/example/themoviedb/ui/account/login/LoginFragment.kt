package com.example.themoviedb.ui.account.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.themoviedb.R
import com.example.themoviedb.databinding.FragmentLoginBinding
import com.example.themoviedb.ui.account.AccountContainerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.signInButton.setOnClickListener {
            if (binding.userLogin.text.isNotEmpty() and binding.userPassword.text.isNotEmpty()) {
                val username = binding.userLogin.text.toString()
                val password = binding.userPassword.text.toString()
                viewModel.loginUser(username, password)
            } else {
                Toast.makeText(requireActivity(), "Fields should not be empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.loginResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                true -> {
                    Toast.makeText(
                        requireActivity(),
                        "Login successful",
                        Toast.LENGTH_SHORT
                    )
                        .show()
//                    openAccountFragment()
                    requireActivity().supportFragmentManager.popBackStack()
                }

                false -> Toast.makeText(
                    requireActivity(),
                    "Invalid login and/or password",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })

        viewModel.username.observe(viewLifecycleOwner, { username ->
            binding.username.text = username
        })
        return binding.root
    }

    private fun openAccountFragment() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.activity_fragment_container,
                AccountContainerFragment.newInstance()
            )
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}