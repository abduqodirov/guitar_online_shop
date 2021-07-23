package com.abduqodirov.guitaronlineshop.data.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.network.IRemoteDataSource
import com.abduqodirov.guitaronlineshop.mockPageProducts
import com.abduqodirov.guitaronlineshop.view.model.SortingFilteringFields

const val PRODUCTS_STARTING_INDEX = 1

class ProductsPagingSource(
    private val dataSource: IRemoteDataSource,
    private val fields: SortingFilteringFields
) : PagingSource<Int, FetchingProduct>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FetchingProduct> {

        return try {
            val currentPageIndex = params.key ?: PRODUCTS_STARTING_INDEX
            // TODO remove mocks
            val response = mockPageProducts
            // val response = dataSource.fetchPaginatedProducts(
            //     pageIndex = currentPageIndex,
            //     limit = PAGE_LIMIT,
            //     fields = fields
            // )

            val nextPageIndex = if (response.products.isEmpty()) {
                null
            } else {
                currentPageIndex + 1
            }

            LoadResult.Page(
                data = response.products,
                prevKey = if (currentPageIndex == PRODUCTS_STARTING_INDEX) null else -1,
                nextKey = nextPageIndex
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, FetchingProduct>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
