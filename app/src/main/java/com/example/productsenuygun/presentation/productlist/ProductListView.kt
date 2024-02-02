package com.example.productsenuygun.presentation.productlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.example.productsenuygun.presentation.common.SearchTextField

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
            onLoadMore = viewModel::loadMoreProducts,
            onQueryChange = viewModel::onQueryChange,
            onSearch = viewModel::onSearch
        )
    }
}

@Composable
fun ProductListContent(
    content: ProductListState.Content,
    onLoadMore: () -> Unit,
    onQueryChange: (query: String) -> Unit,
    onSearch: () -> Unit,
) {
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

    Column(modifier = Modifier.fillMaxSize()) {
        SearchSection(content, onQueryChange, onSearch)
        Spacer(modifier = Modifier.height(16.dp))
        ProductList(lazyListState, content)
    }
}

@Composable
private fun SearchSection(
    content: ProductListState.Content,
    onQueryChange: (query: String) -> Unit,
    onSearch: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        SearchTextField(
            text = content.query,
            onValueChange = onQueryChange,
            onSearch = onSearch,
            errorText = content.queryError
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Default.Face, contentDescription = "Filter")
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Icons.Default.Search"
            )
        }
    }
}

@Composable
private fun ProductList(
    lazyListState: LazyListState,
    content: ProductListState.Content
) {
    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (content.searchState) {
            is SearchState.Loaded -> items(content.searchState.searchedProducts) { product ->
                ProductListItem(product = product, onFavoriteClick = {})
                Spacer(modifier = Modifier.height(16.dp))
            }

            is SearchState.NoResult -> item { Text(text = "No result.") }
            is SearchState.Loading -> item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is SearchState.Empty -> items(content.products) { product ->
                ProductListItem(product = product, onFavoriteClick = {})
                Spacer(modifier = Modifier.height(16.dp))
            }

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
