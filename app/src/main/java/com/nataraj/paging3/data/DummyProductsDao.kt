package com.nataraj.paging3.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @author nataraj-7085
 * @since 07/10/25
 * */
@Dao
interface DummyProductsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<DummyProduct>)

    @Query("SELECT * FROM DummyProduct")
    fun getAllProducts(): PagingSource<Int, DummyProduct>
}