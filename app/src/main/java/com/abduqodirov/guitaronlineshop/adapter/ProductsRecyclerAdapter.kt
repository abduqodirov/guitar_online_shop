package com.abduqodirov.guitaronlineshop.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abduqodirov.guitaronlineshop.databinding.ItemProductBinding
import com.abduqodirov.guitaronlineshop.model.Product

class ProductsRecyclerAdapter(private val productClickListener: ProductClickListener) :
    ListAdapter<Product, ProductsRecyclerAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val binding =
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context), parent, false)

        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = getItem(position)

        holder.binding.itemProductName.text = product.name
        holder.binding.itemProductPrice.text = product.price.toString()
        holder.binding.itemProductDescription.text = product.description

        holder.binding.root.setOnClickListener {
            productClickListener.onProductClick(product)
        }

    }

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    class ProductClickListener(private val productClickListener: (product: Product) -> Unit) {
        fun onProductClick(product: Product) = productClickListener(product)
    }

}