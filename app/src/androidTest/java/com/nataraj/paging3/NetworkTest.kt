package com.nataraj.paging3

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingSource
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nataraj.paging3.data.DummyProduct
import com.nataraj.paging3.data.DummyProductsDao
import com.nataraj.paging3.data.InMemoryDataBase
import com.nataraj.paging3.data.InMemoryDatabaseProvider
import com.nataraj.paging3.network.dummyProductsService
import com.nataraj.paging3.ui.theme.Paging3Theme
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author nataraj-7085
 * @since 06/10/25
 * */
@RunWith(AndroidJUnit4::class)
class NetworkTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var db: InMemoryDataBase
    private lateinit var dao: DummyProductsDao

    @Before
    fun init() {
        mainViewModel = MainViewModel()
        db = InMemoryDatabaseProvider.INSTANCE
        dao = db.dummyProductsDao()
    }

    @Test
    fun testNetworkCall() = runTest {
        // Implement network call tests here
        val response = dummyProductsService.fetchProducts(10, 0)

        assert(response.products.size == 10) { "Expected 10 products, got ${response.products.size}" }
        repeat(10) {
            println(response.products[it].title)
        }
    }

    @Test
    fun pagingSourceTest() = runTest {
        val testData = listOf(
            DummyProduct(1, "Product 1"),
            DummyProduct(2, "Product 2"),
            DummyProduct(3, "Product 3")
        )
        dao.insertAll(testData)

        val pagingSource = dao.getAllProducts()
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 3,
                placeholdersEnabled = false
            )
        )

        Assert.assertTrue(loadResult is PagingSource.LoadResult.Page)
        val page = loadResult as PagingSource.LoadResult.Page
        Assert.assertEquals(testData, page.data)
    }

    @Test
    fun lazyColumnDisplaysData() {
        var pagingItems: LazyPagingItems<DummyProduct>? = null
        composeTestRule.setContent {
            mainViewModel = MainViewModel()
            Paging3Theme {
                UsersScreen(mainViewModel) {
                    pagingItems = it
                }
            }
        }

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            pagingItems?.itemCount == 50
        }

        Assert.assertEquals(50, pagingItems?.itemCount)
    }
}

@Composable
fun UsersScreen(
    mainViewModel: MainViewModel,
    onPagingItemsCollected: (LazyPagingItems<DummyProduct>) -> Unit = {}
) {
    val dummyProductsPagingData = mainViewModel.dummyProductsPager.collectAsLazyPagingItems()

    LaunchedEffect(dummyProductsPagingData.itemCount) {
        onPagingItemsCollected(dummyProductsPagingData)
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn {
                items(
                    count = dummyProductsPagingData.itemCount
                ) { index ->
                    UserPagingListItem(
                        dummyProductsPagingData[index]?.title ?: "-",
                        modifier = Modifier.padding(innerPadding)
                    )
                }

                item {
                    if (dummyProductsPagingData.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }

            val isLoadingState by remember {
                derivedStateOf {
                    dummyProductsPagingData.loadState.refresh is LoadState.Loading
                }
            }

            var isLoading by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(isLoadingState) {
                isLoading = isLoadingState
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}