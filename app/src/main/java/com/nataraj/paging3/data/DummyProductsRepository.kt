package com.nataraj.paging3.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig

/**
 * @author natarajkr007@gmail.com
 * @since 06/10/25
 * */
class DummyProductsRepository {

    private val dummyProductsDao = InMemoryDatabaseProvider.INSTANCE.dummyProductsDao()

    @OptIn(ExperimentalPagingApi::class)
    fun fetchProducts(): Pager<Int, DummyProduct> {
        return Pager(
            config = PAGING_CONFIG,
            remoteMediator = DummyProductsRemoteMediator()
        ) {
            dummyProductsDao.getAllProducts()
        }
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50,
            prefetchDistance = 10
        )
    }
}