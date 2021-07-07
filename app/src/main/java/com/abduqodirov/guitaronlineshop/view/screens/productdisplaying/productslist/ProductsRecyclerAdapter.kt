package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.ItemProductBinding
import com.abduqodirov.guitaronlineshop.view.model.ProductForDisplay
import com.abduqodirov.guitaronlineshop.view.util.loadImageFromNetwork

class ProductsRecyclerAdapter(private val productClickListener: ProductClickListener) :
    ListAdapter<ProductForDisplay, ProductsRecyclerAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val binding =
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = getItem(position)

        if (product.photos.isNotEmpty() && product.photos[0].isNotEmpty()) {

            holder.binding.itemProductImage.loadImageFromNetwork(
                url = product.photos[0],
                errorImg = R.drawable.no_img,
            )
        } else {
            holder.binding.itemProductImage.setImageResource(R.drawable.no_img)
        }

        holder.binding.itemProductName.text = product.name
        holder.binding.itemProductPrice.text = product.price

        holder.binding.itemProductCommentsCountTxt.text = product.comments.size.toString()
        holder.binding.itemProductRatingTxt.text = product.rating

        if (product.comments.isEmpty()) {
            holder.binding.itemProductCommentsGroup.visibility = View.INVISIBLE
        }

        if (product.rating.isEmpty()) {
            holder.binding.itemProductRatingGroup.visibility = View.INVISIBLE
        }

        holder.binding.root.setOnClickListener {
            productClickListener.onProductClick(product)
        }
    }

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ProductDiffCallback : DiffUtil.ItemCallback<ProductForDisplay>() {

        override fun areItemsTheSame(
            oldItem: ProductForDisplay,
            newItem: ProductForDisplay
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductForDisplay,
            newItem: ProductForDisplay
        ): Boolean {
            return oldItem == newItem
        }
    }

    class ProductClickListener(private val productClickListener: (product: ProductForDisplay) -> Unit) {
        fun onProductClick(product: ProductForDisplay) = productClickListener(product)
    }
}