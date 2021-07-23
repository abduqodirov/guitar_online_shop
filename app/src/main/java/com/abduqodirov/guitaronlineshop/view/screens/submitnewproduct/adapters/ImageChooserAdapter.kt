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
        // TODO displays bitmap in the POJO Uploading Image.
        // If image's source is camera so it shows rescaled image.
        //
        holder.binding.itemImage.setImageBitmap(getItem(position).thumbnailBitmap)
        holder.binding.itemRemoveBtn.setOnClickListener {
            Timber.d("Remove command $position")
            callback.onImageRemoved(getItem(position))
        }
    }

    inner class ImageViewHolder(val binding: ItemSubmitImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ImageRemoveCallback(val callback: (image: UploadingImage) -> Unit) {
        fun onImageRemoved(image: UploadingImage) = callback(image)
    }

    class ImageDiffCallback : DiffUtil.ItemCallback<UploadingImage>() {
        override fun areItemsTheSame(oldItem: UploadingImage, newItem: UploadingImage): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UploadingImage, newItem: UploadingImage): Boolean {
            return oldItem == newItem
        }
    }
}
