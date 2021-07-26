package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.ItemProductBinding
import com.abduqodirov.guitaronlineshop.view.model.ProductForDisplay
import com.abduqodirov.guitaronlineshop.view.util.loadImageFromNetwork

class ProductsRecyclerAdapter(private val productClickListener: ProductClickListener) :
    PagingDataAdapter<ProductForDisplay, ProductsRecyclerAdapter.ProductViewHolder>(
        PRODUCT_COMPARATOR
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = getItem(position)

        if (product != null) {
            populateCardWithData(product, holder)

            holder.binding.root.setOnClickListener {
                productClickListener.onProductClick(product)
            }
        }
    }

    private fun populateCardWithData(product: ProductForDisplay, holder: ProductViewHolder) {

        holder.binding.run {

            if (product.photos.isNotEmpty()) {

                itemProductImage.loadImageFromNetwork(
                    url = product.photos[0],
                    errorImg = R.drawable.no_img,
                )
            } else {
                itemProductImage.setImageResource(R.drawable.no_img)
            }

            itemProductName.text = product.name
            itemProductPrice.text = product.price

            itemProductCommentsCountTxt.text = product.comments.size.toString()
            itemProductRatingTxt.text = product.rating

            itemProductCommentsGroup.isVisible = product.comments.isNotEmpty()
            itemProductRatingGroup.isVisible = product.rating.isNotEmpty()
        }
    }

    inner class ProductViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val PRODUCT_COMPARATOR = ProductDiffCallback()
    }

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
