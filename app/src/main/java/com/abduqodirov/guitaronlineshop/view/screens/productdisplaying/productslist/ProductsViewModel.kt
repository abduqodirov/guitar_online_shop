package com.abduqodirov.guitaronlineshop.view.screens.productdisplaying.productslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.repository.fetching.ProductsFetchingRepository
import com.abduqodirov.guitaronlineshop.view.mapper.mapFetchedProduct
import com.abduqodirov.guitaronlineshop.view.model.ProductForDisplay
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsFetchingRepository
) : ViewModel() {

    private var paginatedProducts: Flow<PagingData<ProductForDisplay>>? = null

    val products = paginatedProducts?.asLiveData()

    fun fetchProducts(fields: SortingFilteringFields): Flow<PagingData<ProductForDisplay>> {

        val newResult: Flow<PagingData<ProductForDisplay>> =
            productsRepository.fetchPaginatedProducts(fields).map { value: PagingData<FetchingProduct> ->
                value.map {
                    mapFetchedProduct(it)
                }
            }.cachedIn(viewModelScope)

        paginatedProducts = newResult

        return newResult
    }
}
