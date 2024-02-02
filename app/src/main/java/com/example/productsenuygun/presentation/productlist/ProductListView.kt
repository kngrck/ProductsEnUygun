package com.example.productsenuygun.presentation.productlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.productsenuygun.presentation.common.ErrorView
import com.example.productsenuygun.presentation.common.LoadingIndicator
import com.example.productsenuygun.presentation.common.ProductListItem

@Composable
fun ProductListingView(
    navController: NavController,
    viewModel: ProductListViewModel = hiltViewModel()
) {

    when (val state = viewModel.state.collectAsState().value) {
        is ProductListState.Loading -> LoadingIndicator()
        is ProductListState.Error -> ErrorView(state.message)
        is ProductListState.Content -> ProductListContent(
            state,
            onLoadMore = viewModel::loadMoreProducts
        )
    }
}

@Composable
fun ProductListContent(content: ProductListState.Content, onLoadMore: () -> Unit) {
    val lazyListState = rememberLazyListState()
    val shouldLoadMore = remember {
        derivedStateOf {
            !lazyListState.isScrollInProgress
                    && !lazyListState.canScrollForward
                    && !content.isLastPage
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(content.products) { product ->
            ProductListItem(product = product, onFavoriteClick = {})
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (content.pageLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}
