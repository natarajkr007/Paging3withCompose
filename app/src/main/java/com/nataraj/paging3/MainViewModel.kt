package com.nataraj.paging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import com.nataraj.paging3.data.DummyProduct
import com.nataraj.paging3.data.DummyProductsPagingSource
import com.nataraj.paging3.data.DummyProductsRepository
import com.nataraj.paging3.data.InMemoryDatabaseProvider
import kotlinx.coroutines.launch

/**
 * @author natarajkr007@gmail.com
 * @since 06/10/25
 * */
class MainViewModel : ViewModel() {
    /**
     * to create pager using the repository un-comment the below line
     * */
//    val dummyProductsPager = DummyProductsRepository().fetchProducts().flow.cachedIn(viewModelScope)

    /**
     * to create pager without using the repository un-comment the below line
     * */
    val dummyProductsPager = Pager(
        config = DummyProductsRepository.PAGING_CONFIG,
        initialKey = 0,
        pagingSourceFactory = { DummyProductsPagingSource() }
    ).flow.cachedIn(viewModelScope)

    fun markItClicked(dummyProduct: DummyProduct?) {
        if (dummyProduct == null) return

        viewModelScope.launch {
            InMemoryDatabaseProvider.INSTANCE.dummyProductsDao().markItChecked(dummyProduct)
        }
    }
}