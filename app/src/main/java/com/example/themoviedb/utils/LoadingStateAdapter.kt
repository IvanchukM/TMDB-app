package com.example.themoviedb.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.themoviedb.databinding.FooterItemBinding

class LoadingStateAdapter(private val onRetryClickListener: OnRetryClickListener) :
    LoadStateAdapter<LoadingStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val binding = FooterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, onRetryClickListener)
    }

    interface OnRetryClickListener {
        fun onRetryClick()
    }
}

class LoadingStateViewHolder(
    private val binding: FooterItemBinding,
    private val onRetryClickListener: LoadingStateAdapter.OnRetryClickListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(loadState: LoadState) {

        binding.progressBar.visibility =
            if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
        binding.errorMsg.visibility = if (loadState is LoadState.Error) View.VISIBLE else View.GONE
        binding.retryButton.visibility =
            if (loadState is LoadState.Error) View.VISIBLE else View.GONE

        binding.retryButton.setOnClickListener {
            onRetryClickListener.onRetryClick()
        }
    }
}
