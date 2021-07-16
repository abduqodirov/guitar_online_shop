package com.abduqodirov.guitaronlineshop.data.network.implementations

import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.model.PageProducts
import com.abduqodirov.guitaronlineshop.data.model.SendingProductWithUploadedImages
import com.abduqodirov.guitaronlineshop.data.network.IRemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.retrofit.ShopService
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields

class RemoteDataSourceImpl(private val shopService: ShopService) : IRemoteDataSource {

    override suspend fun fetchProducts(): List<FetchingProduct> {
        return shopService.fetchProducts()
    }

    override suspend fun fetchPaginatedProducts(
        pageIndex: Int,
        limit: Int,
        fields: SortingFilteringFields
    ): PageProducts {
        return shopService.fetchPaginatedProducts(
            pageIndex = pageIndex,
            limit = limit,
            lowPrice = fields.lowPrice,
            highPrice = fields.highPrice,
            sortedBy = fields.sortBy,
            nameFilter = fields.nameFilter,
            orderOfSort = fields.order
        )
    }

    override suspend fun fetchProductById(id: String): FetchingProduct {
        return shopService.fetchProductById(id)
    }

    override suspend fun submitProduct(product: SendingProductWithUploadedImages): FetchingProduct {
        return shopService.submitProduct(product)
    }
}
