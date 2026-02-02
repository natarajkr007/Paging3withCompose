package com.nataraj.paging3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
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
                            .padding(
                                innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                                0.dp,
                                innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                                0.dp
                            )
                            .fillMaxSize()
                    ) {
                        LazyColumn {
                            items(
                                count = dummyProductsPagingData.itemCount
                            ) { index ->
                                if (index == 0) {
                                    Spacer(modifier = Modifier.size(innerPadding.calculateTopPadding()))
                                }

                                val dummyProduct = dummyProductsPagingData[index]
                                UserPagingListItem(
                                    dummyProduct?.title ?: "-",
                                    isClicked = dummyProduct?.isClicked ?: false,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                        .clickable(
                                            onClick = {
                                                mainViewModel.markItClicked(
                                                    dummyProductsPagingData[index]?.copy(title = "clicked")
                                                )
                                            }
                                        )
                                )
                            }

                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (dummyProductsPagingData.loadState.append is LoadState.Loading) {
                                        CircularProgressIndicator()
                                    } else {
                                        Text(
                                            stringResource(R.string.list_end_message),
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.size(innerPadding.calculateBottomPadding() + 16.dp))
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
fun UserPagingListItem(text: String, isClicked: Boolean, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
    ) {
        Text(text, Modifier.weight(1F))

        if (isClicked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "clicked",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserPagingListItemPreview() {
    Paging3Theme {
        UserPagingListItem("No data", false, Modifier.fillMaxWidth())
    }
}