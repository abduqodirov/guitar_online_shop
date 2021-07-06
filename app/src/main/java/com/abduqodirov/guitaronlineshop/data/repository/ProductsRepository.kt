package com.abduqodirov.guitaronlineshop.data.repository

interface ProductsRepository {

    fun fetchProducts()

    fun fetchProductById(id: String)
}
