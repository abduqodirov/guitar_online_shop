package com.abduqodirov.guitaronlineshop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.ItemProductBinding
import com.abduqodirov.guitaronlineshop.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.model.Product
import com.bumptech.glide.Glide

class ProductsRecyclerAdapter(private val productClickListener: ProductClickListener) :
    ListAdapter<Product, ProductsRecyclerAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val binding =
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = getItem(position) as FetchingProduct


        if (product.photos.isNotEmpty() && product.photos[0].isNotEmpty()) {
            Glide.with(holder.binding.itemProductImage.context)
                .load(product.photos[0])
                .error(R.drawable.no_img)
                .into(holder.binding.itemProductImage)
        } else {
            holder.binding.itemProductImage.setImageResource(R.drawable.no_img)
        }

        holder.binding.itemProductName.text = product.name
        holder.binding.itemProductPrice.text = product.price.toString()

        holder.binding.itemProductCommentsCountTxt.text = product.comments.size.toString()
        holder.binding.itemProductRatingTxt.text = product.rating.average().toString()

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

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return (oldItem as FetchingProduct).id == (newItem as FetchingProduct).id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return (oldItem as FetchingProduct) == (newItem as FetchingProduct)
        }
    }

    class ProductClickListener(private val productClickListener: (product: Product) -> Unit) {
        fun onProductClick(product: Product) = productClickListener(product)
    }

}