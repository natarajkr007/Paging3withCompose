package com.nataraj.paging3.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.nataraj.paging3.network.dummyProductsService

/**
 * @author nataraj-7085
 * @since 07/10/25
 * */
@OptIn(ExperimentalPagingApi::class)
class DummyProductsRemoteMediator : RemoteMediator<Int, DummyProduct>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DummyProduct>
    ): MediatorResult {
        return try {
            val skip = when (loadType) {
                LoadType.REFRESH -> 0

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val itemsLoaded = state.pages.sumOf { it.data.size }
                    itemsLoaded
                }
            }

            val response = dummyProductsService.fetchProducts(
                state.config.pageSize,
                skip
            )

            InMemoryDatabaseProvider.INSTANCE.dummyProductsDao().apply {
                insertAll(response.products)
            }

            MediatorResult.Success(
                endOfPaginationReached = response.skip + response.limit >= response.total
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}