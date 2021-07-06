package com.abduqodirov.guitaronlineshop.data.network

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct

interface IRemoteDataSource {

    suspend fun fetchProducts(): List<FetchingProduct>
}
