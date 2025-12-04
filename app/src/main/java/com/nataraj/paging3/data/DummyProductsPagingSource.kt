package com.nataraj.paging3.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nataraj.paging3.network.dummyProductsService

class DummyProductsPagingSource : PagingSource<Int, DummyProduct>() {
    override fun getRefreshKey(state: PagingState<Int, DummyProduct>): Int {
        return ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2).coerceAtLeast(0)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DummyProduct> {
        return when (params) {
            is LoadParams.Refresh<Int> -> {
                Log.d(
                    this::class.simpleName,
                    "refresh - key ${params.key} & loadSize - ${params.loadSize}"
                )
                fetchDummyProducts(0, params.loadSize)
            }

            is LoadParams.Append<Int> -> {
                Log.d(
                    this::class.simpleName,
                    "append - key ${params.key} & loadSize - ${params.loadSize}"
                )
                fetchDummyProducts(params.key, params.loadSize)
            }

            is LoadParams.Prepend<Int> -> LoadResult.Invalid()
        }
    }

    private suspend fun fetchDummyProducts(key: Int, limit: Int): LoadResult<Int, DummyProduct> {
        return try {
            val response = dummyProductsService.fetchProducts(limit, key)

            LoadResult.Page(
                data = response.products,
                prevKey = null,
                nextKey = (key + response.products.size).takeIf { nextKey ->
                    nextKey < response.total
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}