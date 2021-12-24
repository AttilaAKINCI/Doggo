package com.akinci.doggoapp.feature.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akinci.doggoapp.databinding.RowShimmerBinding

class ShimmerAdapter(
    private val randomItemCount: Int = (3..6).random()
): ListAdapter<String, ShimmerAdapter.ShimmerViewHolder>(ShimmerDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShimmerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ShimmerViewHolder(RowShimmerBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int { return randomItemCount }
    override fun onBindViewHolder(holder: ShimmerViewHolder, position: Int) { holder.bind() }

    class ShimmerViewHolder(val binding: RowShimmerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() { binding.shimmerViewContainer.startShimmer() }
    }
}

class ShimmerDiffCallBack : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}