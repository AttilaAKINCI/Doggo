package com.akinci.doggoapp.feature.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.marginStart
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akinci.doggoapp.R
import com.akinci.doggoapp.databinding.RowBreedBinding
import com.akinci.doggoapp.feature.dashboard.data.Breed

class BreedListAdapter(
    private val clickListener: (Breed, Int) -> Unit
): ListAdapter<Breed, BreedListAdapter.BreedViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return BreedViewHolder(RowBreedBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item,
            position,
            clickListener
        )
    }

    class BreedViewHolder(private val binding: RowBreedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Breed, position: Int, clickListener: (Breed, Int) -> Unit) {
            binding.data = data
            binding.breedCardView.setOnClickListener { clickListener.invoke(data, position) }
            binding.executePendingBindings()
        }
    }
}

class DiffCallBack : DiffUtil.ItemCallback<Breed>() {
    override fun areItemsTheSame(oldItem: Breed, newItem: Breed): Boolean {
        return (oldItem.name == newItem.name)
    }

    override fun areContentsTheSame(oldItem: Breed, newItem: Breed): Boolean {
        return oldItem == newItem
    }
}