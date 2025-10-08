package com.nataraj.paging3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.nataraj.paging3.ui.theme.Paging3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val mainViewModel: MainViewModel by viewModels<MainViewModel>()

        setContent {

            val dummyProductsPagingData =
                mainViewModel.dummyProductsPager.collectAsLazyPagingItems()

            Paging3Theme {
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
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
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
        }
    }
}

@Composable
fun UserPagingListItem(text: String, modifier: Modifier = Modifier) {
    Text(text, modifier)
}

@Preview(showBackground = true)
@Composable
fun UserPagingListItemPreview() {
    Paging3Theme {
        UserPagingListItem("No data")
    }
}