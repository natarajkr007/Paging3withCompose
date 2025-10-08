package com.nataraj.paging3.network

import com.nataraj.paging3.data.DummyProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author nataraj-7085
 * @since 06/10/25
 * */
interface DummyProductsService {

    /* https://dummyjson.com/products?limit=1&skip=1 */
    @GET("/products")
    suspend fun fetchProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): DummyProductResponse
}