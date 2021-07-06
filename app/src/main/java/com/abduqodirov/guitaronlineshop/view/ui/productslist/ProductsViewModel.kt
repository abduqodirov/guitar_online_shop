package com.abduqodirov.guitaronlineshop.view.ui.productslist

import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepositoryImpl
import javax.inject.Inject
import javax.inject.Named

// TODO named kerakmi?
@Named("list")
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsFetchingRepository
) : ViewModel() {

    val products = (productsRepository as ProductsFetchingRepositoryImpl).products

    fun refreshProducts() {
        productsRepository.fetchProducts()
    }
}
