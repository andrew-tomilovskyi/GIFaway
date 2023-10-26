package com.example.gifviewer.presentation.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gifviewer.R
import com.example.gifviewer.databinding.ItemFullScreenGifBinding
import com.example.gifviewer.databinding.ItemGifBinding
import com.example.gifviewer.util.glideSetFullScreenGif
import com.example.gifviewer.util.glideSetGif

class GifAdapter(
    private val onClick: ((Int) -> Unit)
) : ListAdapter<GifAdapter.AdapterItem, RecyclerView.ViewHolder>(diffCallback) {

    sealed class AdapterItem {
        data class GifItem(val url: String) : AdapterItem()
        data class FullScreenGifItem(val url: String) : AdapterItem()
    }

    inner class GifViewHolder(private val binding: ItemGifBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String, position: Int) = with(binding) {

            glideSetGif(gifImageView.context, url, gifImageView)

            root.setOnClickListener {
                onClick(position)
            }
        }
    }

    inner class FullScreenGifViewHolder(private val binding: ItemFullScreenGifBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) = with(binding) {
            glideSetFullScreenGif(fullScreenGifImageView.context, url, fullScreenGifImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_gif -> {
                val binding =
                    ItemGifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                GifViewHolder(binding)
            }
            R.layout.item_full_screen_gif -> {
                val binding = ItemFullScreenGifBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FullScreenGifViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is AdapterItem.GifItem -> {
                (holder as GifViewHolder).bind(item.url, position)
            }
            is AdapterItem.FullScreenGifItem -> {
                (holder as FullScreenGifViewHolder).bind(item.url)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = when (getItem(position)) {
            is AdapterItem.GifItem -> R.layout.item_gif
            is AdapterItem.FullScreenGifItem -> R.layout.item_full_screen_gif
            else -> throw IllegalArgumentException("Invalid item type at position $position")
        }
        return viewType
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<AdapterItem>() {
            override fun areItemsTheSame(
                oldItem: AdapterItem,
                newItem: AdapterItem
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: AdapterItem,
                newItem: AdapterItem
            ): Boolean =
                oldItem.hashCode() == newItem.hashCode()
        }
    }
}