package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productdetails.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abduqodirov.guitaronlineshop.databinding.ItemCommentBinding

class CommentsRecyclerAdapter :
    ListAdapter<String, CommentsRecyclerAdapter.CommentsViewHolder>(CommentDiffCallback()) {

    var collapseComments = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {

        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CommentsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.binding.itemCommentText.text = getItem(position)
        if (collapseComments) {
            holder.binding.itemCommentText.maxLines = 1
        }
    }

    inner class CommentsViewHolder(val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root)

    class CommentDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
