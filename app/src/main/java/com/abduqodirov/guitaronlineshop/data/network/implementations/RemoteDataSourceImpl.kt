package com.abduqodirov.guitaronlineshop.data.network.implementations

import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.PageProductsDTO
import com.abduqodirov.guitaronlineshop.data.model.SendingProductWithUploadedImagesDTO
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.retrofit.ShopService
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields

class RemoteDataSourceImpl(private val shopService: ShopService) : RemoteDataSource {

    override suspend fun fetchProducts(): List<FetchingProductDTO> {
        return shopService.fetchProducts()
    }

    override suspend fun fetchPaginatedProducts(
        pageIndex: Int,
        limit: Int,
        fields: SortingFilteringFields
    ): PageProductsDTO {
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

    override suspend fun fetchProductById(id: String): FetchingProductDTO {
        return shopService.fetchProductById(id)
    }

    override suspend fun submitProduct(product: SendingProductWithUploadedImagesDTO): FetchingProductDTO {
        return shopService.submitProduct(product)
    }
}
