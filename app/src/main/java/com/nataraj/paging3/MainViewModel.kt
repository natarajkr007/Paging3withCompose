package com.nataraj.paging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.nataraj.paging3.data.DummyProductsRepository

/**
 * @author natarajkr007@gmail.com
 * @since 06/10/25
 * */
class MainViewModel : ViewModel() {
    val dummyProductsPager = DummyProductsRepository().fetchProducts().flow.cachedIn(viewModelScope)
}