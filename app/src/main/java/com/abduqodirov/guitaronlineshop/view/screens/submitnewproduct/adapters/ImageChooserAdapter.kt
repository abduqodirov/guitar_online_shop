package com.abduqodirov.guitaronlineshop.view.screens.submitnewproduct.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abduqodirov.guitaronlineshop.databinding.ItemSubmitImageBinding
import com.abduqodirov.guitaronlineshop.view.model.UploadingImage
import timber.log.Timber

class ImageChooserAdapter(private val callback: ImageRemoveCallback) :
    ListAdapter<UploadingImage, ImageChooserAdapter.ImageViewHolder>(ImageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            ItemSubmitImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.itemImage.setImageBitmap(getItem(position).bitmap)
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

    class ImageDiffCallback : DiffUtil.ItemCallback<UploadingImage>() {
        override fun areItemsTheSame(oldItem: UploadingImage, newItem: UploadingImage): Boolean {
            return oldItem.path == newItem.path
        }

        override fun areContentsTheSame(oldItem: UploadingImage, newItem: UploadingImage): Boolean {
            return oldItem == newItem
        }
    }
}
