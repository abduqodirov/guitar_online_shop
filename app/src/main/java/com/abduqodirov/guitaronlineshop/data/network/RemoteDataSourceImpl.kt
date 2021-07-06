package com.abduqodirov.guitaronlineshop.data.network

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct

class RemoteDataSourceImpl(private val shopService: ShopService) : IRemoteDataSource {

    override suspend fun fetchProducts(): List<FetchingProduct> {
        return shopService.fetchProducts()
    }
}
