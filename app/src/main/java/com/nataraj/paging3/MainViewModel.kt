package com.nataraj.paging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.nataraj.paging3.data.DummyProduct
import com.nataraj.paging3.data.DummyProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

/**
 * @author natarajkr007@gmail.com
 * @since 06/10/25
 * */
class MainViewModel : ViewModel() {

    val mutations = MutableStateFlow<List<Int>>(emptyList())

    /**
     * to create pager using the repository un-comment the below line
     * */
    val dummyProductsPager = DummyProductsRepository().fetchProducts()
        .flow.cachedIn(viewModelScope)
        .combine(mutations) { pagingData, muts ->
            pagingData.map { dummyProduct ->
                if (muts.contains(dummyProduct.id)) {
                    val isClicked = dummyProduct.isClicked
                    dummyProduct.copy(isClicked = !isClicked)
                } else {
                    dummyProduct
                }
            }
        }

    fun markItClicked(dummyProduct: DummyProduct?) {
        if (dummyProduct == null) return

        mutations.value = mutations.value.toMutableList().apply {
            if (contains(dummyProduct.id)) {
                remove(dummyProduct.id)
            } else {
                add(dummyProduct.id)
            }
        }

//        viewModelScope.launch {
//            InMemoryDatabaseProvider.INSTANCE.dummyProductsDao().markItChecked(dummyProduct)
//        }
    }
}