package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.recycleradapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.abduqodirov.guitaronlineshop.R
import com.abduqodirov.guitaronlineshop.databinding.ItemFooterProductsLoadingBinding

class ProductsLoadStateViewHolder(
    private val binding: ItemFooterProductsLoadingBinding,
    private val retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): ProductsLoadStateViewHolder {
            Log.d("lstt", "create: loadStateViewholder creating requested")

            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_footer_products_loading, parent, false)
            val binding = ItemFooterProductsLoadingBinding.bind(view)

            return ProductsLoadStateViewHolder(binding, retry)
        }
    }
}
