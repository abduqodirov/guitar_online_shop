package com.abduqodirov.guitaronlineshop.data.network.implementations

import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.PageProductsDTO
import com.abduqodirov.guitaronlineshop.data.model.SendingProductWithUploadedImagesDTO
import com.abduqodirov.guitaronlineshop.data.model.TokenUserDTO
import com.abduqodirov.guitaronlineshop.data.network.LOGIN_KEY_EMAIL
import com.abduqodirov.guitaronlineshop.data.network.LOGIN_KEY_PASSWORD
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.retrofit.ShopService
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields
import timber.log.Timber
import javax.inject.Inject
import kotlin.Exception

class RemoteDataSourceImpl @Inject constructor(private val shopService: ShopService) : RemoteDataSource {

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
        try {
            val submitProduct = shopService.submitProduct(product)
            return submitProduct
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.d("error bo'ldi jo'natishda")
            return shopService.submitProduct(product)
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): TokenUserDTO {
        val map = mapOf(
            LOGIN_KEY_EMAIL to email,
            LOGIN_KEY_PASSWORD to password
        )
        return shopService.loginWithEmail(map)
    }

    override suspend fun signUpWithEmail(email: String, password: String): TokenUserDTO {
        val map = mapOf(
            LOGIN_KEY_EMAIL to email,
            LOGIN_KEY_PASSWORD to password
        )
        return shopService.signUpWithEmail(map)
    }
}
