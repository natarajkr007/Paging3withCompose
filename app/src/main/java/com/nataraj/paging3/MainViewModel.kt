package com.nataraj.paging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.nataraj.paging3.data.DummyProduct
import com.nataraj.paging3.data.DummyProductsPagingSource
import com.nataraj.paging3.data.DummyProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

/**
 * @author natarajkr007@gmail.com
 * @since 06/10/25
 * */
class MainViewModel : ViewModel() {
    /**
     * to create pager using the repository un-comment the below line
     * */
//    val dummyProductsPager = DummyProductsRepository().fetchProducts().flow.cachedIn(viewModelScope)

    val mutations = MutableStateFlow<Map<Int, Boolean>>(emptyMap())

    /**
     * to create pager without using the repository un-comment the below line
     * */
    val dummyProductsPager = Pager(
        config = DummyProductsRepository.PAGING_CONFIG,
        initialKey = 0,
        pagingSourceFactory = { DummyProductsPagingSource() }
    ).flow.cachedIn(viewModelScope)
        .combine(mutations) { pagingData, muts ->
            pagingData.map { dummyProduct ->
                if (muts.containsKey(dummyProduct.id)) {
                    dummyProduct.copy(title = "clicked")
                } else {
                    dummyProduct
                }
            }
        }

    fun markItClicked(dummyProduct: DummyProduct?) {
        if (dummyProduct == null) return

        val currentMuts = mutations.value.toMutableMap()
        currentMuts[dummyProduct.id] = true
        mutations.value = currentMuts

//        viewModelScope.launch {
//            InMemoryDatabaseProvider.INSTANCE.dummyProductsDao().markItChecked(dummyProduct)
//        }
    }
}