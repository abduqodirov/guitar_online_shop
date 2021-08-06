package com.abduqodirov.guitaronlineshop.data.network.implementations

import com.abduqodirov.guitaronlineshop.data.model.ErrorMessageDTO
import com.abduqodirov.guitaronlineshop.data.model.FetchingProductDTO
import com.abduqodirov.guitaronlineshop.data.model.PageProductsDTO
import com.abduqodirov.guitaronlineshop.data.model.Response
import com.abduqodirov.guitaronlineshop.data.model.SendingProductWithUploadedImagesDTO
import com.abduqodirov.guitaronlineshop.data.model.TokenUserDTO
import com.abduqodirov.guitaronlineshop.data.network.LOGIN_KEY_EMAIL
import com.abduqodirov.guitaronlineshop.data.network.LOGIN_KEY_PASSWORD
import com.abduqodirov.guitaronlineshop.data.network.RemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.retrofit.ShopService
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val shopService: ShopService) :
    RemoteDataSource {

    private val moshi = Moshi.Builder().build()
    private val adapter: JsonAdapter<ErrorMessageDTO> = moshi.adapter(ErrorMessageDTO::class.java)

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

    override suspend fun submitProduct(product: SendingProductWithUploadedImagesDTO): Response<FetchingProductDTO> {
        try {
            return Response.Success(shopService.submitProduct(product))
        } catch (httpException: HttpException) {
            return withContext(Dispatchers.IO) {
                val errorResponse =
                    adapter.fromJson(httpException.response()?.errorBody()?.string())
                return@withContext Response.Failure(errorMessage = errorResponse?.message)
            }
        } catch (e: Exception) {
            return Response.Failure(e.localizedMessage)
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): Response<TokenUserDTO> {
        val map = mapOf(
            LOGIN_KEY_EMAIL to email,
            LOGIN_KEY_PASSWORD to password
        )
        try {
            return Response.Success(shopService.loginWithEmail(map))
        } catch (httpException: HttpException) {

            return withContext(Dispatchers.IO) {
                val errorResponse =
                    adapter.fromJson(httpException.response()?.errorBody()?.string())
                return@withContext Response.Failure(errorMessage = errorResponse?.message)
            }
        } catch (e: Exception) {
            return Response.Failure(e.localizedMessage)
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String): Response<TokenUserDTO> {
        val map = mapOf(
            LOGIN_KEY_EMAIL to email,
            LOGIN_KEY_PASSWORD to password
        )
        try {
            return Response.Success(shopService.signUpWithEmail(map))
        } catch (httpException: HttpException) {
            return withContext(Dispatchers.IO) {
                val errorResponse =
                    adapter.fromJson(httpException.response()?.errorBody()?.string())
                return@withContext Response.Failure(errorResponse?.message)
            }
        } catch (e: Exception) {
            return Response.Failure(e.localizedMessage)
        }
    }

    override suspend fun refreshToken(): Response<TokenUserDTO> {
        try {
            return Response.Success(shopService.refreshToken())
        } catch (httpException: HttpException) {
            return withContext(Dispatchers.IO) {
                val errorResponse =
                    adapter.fromJson(httpException.response()?.errorBody()?.string())
                return@withContext Response.Failure(errorResponse?.message)
            }
        } catch (e: Exception) {
            return Response.Failure(e.localizedMessage)
        }
    }
}
