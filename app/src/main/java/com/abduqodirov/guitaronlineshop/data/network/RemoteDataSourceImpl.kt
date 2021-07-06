package com.abduqodirov.guitaronlineshop.data.network

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.SendingProduct

class RemoteDataSourceImpl(private val shopService: ShopService) : IRemoteDataSource {

    override suspend fun fetchProducts(): List<FetchingProduct> {
        return shopService.fetchProducts()
    }

    override suspend fun fetchProductById(id: String): FetchingProduct {
        return shopService.fetchProductById(id)
    }

    override suspend fun submitProduct(product: SendingProduct): FetchingProduct {
        return shopService.submitProduct(product)
    }
}
