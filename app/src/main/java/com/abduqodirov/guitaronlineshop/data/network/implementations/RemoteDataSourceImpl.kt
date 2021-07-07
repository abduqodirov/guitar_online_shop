package com.abduqodirov.guitaronlineshop.data.network.implementations

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.SendingProductWithUploadedImages
import com.abduqodirov.guitaronlineshop.data.network.IRemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.retrofit.ShopService

class RemoteDataSourceImpl(private val shopService: ShopService) : IRemoteDataSource {

    override suspend fun fetchProducts(): List<FetchingProduct> {
        return shopService.fetchProducts()
    }

    override suspend fun fetchProductById(id: String): FetchingProduct {
        return shopService.fetchProductById(id)
    }

    override suspend fun submitProduct(product: SendingProductWithUploadedImages): FetchingProduct {
        return shopService.submitProduct(product)
    }
}
