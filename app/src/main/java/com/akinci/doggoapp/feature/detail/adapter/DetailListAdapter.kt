package com.akinci.doggoapp.feature.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.ImageRequest
import coil.request.ImageResult
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.akinci.doggoapp.R
import com.akinci.doggoapp.databinding.RowDetailBinding

class DetailListAdapter: ListAdapter<String, DetailListAdapter.DetailViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DetailViewHolder(RowDetailBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class DetailViewHolder(private val binding: RowDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: String) {
            binding.doggoImageView.load(data) {
                crossfade(true)
                listener(object :ImageRequest.Listener{
                    override fun onSuccess(request: ImageRequest, metadata: ImageResult.Metadata) {
                        super.onSuccess(request, metadata)
                        binding.doggoNameTextView.text = DoggoNameProvider.getRandomName()
                    }
                })
            }
        }
    }
}

class DiffCallBack : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}