package com.abduqodirov.guitaronlineshop.data.repository.fetching

interface ProductsFetchingRepository {

    fun fetchProducts()

    fun fetchProductById(id: String)
}
