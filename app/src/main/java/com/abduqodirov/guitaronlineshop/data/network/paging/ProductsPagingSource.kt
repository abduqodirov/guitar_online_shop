package com.abduqodirov.guitaronlineshop.data.network.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.abduqodirov.guitaronlineshop.data.model.FetchingProduct
import com.abduqodirov.guitaronlineshop.data.network.IRemoteDataSource
import com.abduqodirov.guitaronlineshop.data.network.PAGE_LIMIT

const val PRODUCTS_STARTING_INDEX = 1

class ProductsPagingSource(
    private val remoteDataSource: IRemoteDataSource
) : PagingSource<Int, FetchingProduct>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FetchingProduct> {

        Log.d("pagingda", "load: ${params.loadSize}")

        return try {
            val currentPageIndex = params.key ?: PRODUCTS_STARTING_INDEX
            val response = remoteDataSource.fetchPaginatedProducts(
                pageIndex = currentPageIndex,
                limit = PAGE_LIMIT
            )

            // TODO ohirgi pagega kelganimizni totalCount orqali tekshirib olsa bo'ladi. totalCount - params.loadSize
            val nextPageIndex = if (response.products.isEmpty()) {
                null
            } else {
                currentPageIndex + 1
                // TODO duplicate ketib qolyapti, to'g'rilash kerak
            }

            LoadResult.Page(
                data = response.products,
                prevKey = if (currentPageIndex == PRODUCTS_STARTING_INDEX) null else -1,
                nextKey = nextPageIndex
            )
        } catch (e: Exception) {
            // TODO error olinganini hisobga olmagan UI
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
