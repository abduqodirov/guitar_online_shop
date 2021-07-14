package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist.recycleradapters

import android.util.Log
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class ProductsLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ProductsLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ProductsLoadStateViewHolder {
        Log.d("lstt", "onCreateViewHolder: ")
        return ProductsLoadStateViewHolder.create(parent, retry)
    }

    override fun onBindViewHolder(holder: ProductsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}
