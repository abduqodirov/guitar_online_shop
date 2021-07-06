package com.abduqodirov.guitaronlineshop.view.ui.productslist

import androidx.lifecycle.ViewModel
import com.abduqodirov.guitaronlineshop.data.repository.ProductsRepository
import com.abduqodirov.guitaronlineshop.data.repository.ProductsRepositoryImpl
import javax.inject.Inject
import javax.inject.Named

@Named("list")
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    val products = (productsRepository as ProductsRepositoryImpl).products

    fun refreshProducts() {
        productsRepository.fetchProducts()
    }
}
