package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abduqodirov.guitaronlineshop.databinding.ItemSubmitImageBinding
import timber.log.Timber

class ImageChooserAdapter(private val callback: ImageRemoveCallback) :
    ListAdapter<Bitmap, ImageChooserAdapter.ImageViewHolder>(ImageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            ItemSubmitImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.itemImage.setImageBitmap(getItem(position))
        holder.binding.itemRemoveBtn.setOnClickListener {
            Timber.d("Remove command $position")
            callback.onImageRemoved(position)
        }
    }

    inner class ImageViewHolder(val binding: ItemSubmitImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ImageRemoveCallback(val callback: (position: Int) -> Unit) {
        fun onImageRemoved(position: Int) = callback(position)
    }

    class ImageDiffCallback : DiffUtil.ItemCallback<Bitmap>() {
        override fun areItemsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean {
            return oldItem.sameAs(newItem)
        }
    }
}
