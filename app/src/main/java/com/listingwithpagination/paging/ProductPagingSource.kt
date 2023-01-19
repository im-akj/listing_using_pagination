package com.listingwithpagination.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.listingwithpagination.api.ApiService
import com.listingwithpagination.model.Product

class ProductPagingSource(private val apiService: ApiService) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val currentPage = params.key ?: 1
            val response = apiService.getProducts(currentPage)
            val data = response.body()?.response?.products ?: emptyList()
            if(data.isNotEmpty()) {
                val responseData = mutableListOf<Product>()
                responseData.addAll(data)

                LoadResult.Page(
                    data = responseData,
                    prevKey = if (currentPage == 1) null else -1,
                    nextKey = currentPage.plus(1))
            } else {
                LoadResult.Error(java.lang.RuntimeException("No more data"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }

    }
}