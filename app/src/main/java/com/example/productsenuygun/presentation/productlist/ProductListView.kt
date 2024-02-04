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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.productsenuygun.domain.model.filter.Category
import com.example.productsenuygun.domain.model.filter.SortType
import com.example.productsenuygun.presentation.common.ErrorView
import com.example.productsenuygun.presentation.common.LoadingIndicator
import com.example.productsenuygun.presentation.common.ProductListItem
import com.example.productsenuygun.presentation.common.SearchTextField
import com.example.productsenuygun.presentation.navigation.NavigationItem

@Composable
fun ProductListingView(
    navController: NavController,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initProductList()
    }

    when (val state = viewModel.state.collectAsState().value) {
        is ProductListState.Loading -> LoadingIndicator()
        is ProductListState.Error -> ErrorView(state.message)
        is ProductListState.Content -> ProductListContent(
            state,
            onLoadMore = viewModel::loadMoreProducts,
            onQueryChange = viewModel::onQueryChange,
            onSearch = viewModel::onSearch,
            onProductClick = {
                navController.navigate("${NavigationItem.ProductDetail.route}/${it}")
            },
            onSortSelect = viewModel::onSortSelect,
            onCategorySelect = viewModel::onCategorySelect,
            onApplyFilters = viewModel::onApplyFilters,
            onResetFilters = viewModel::onResetFilters,
            onDismissFilters = viewModel::onDismissFilters,
            onOpenFilters = viewModel::onOpenFilters,
        )
    }
}

@Composable
fun ProductListContent(
    content: ProductListState.Content,
    onLoadMore: () -> Unit,
    onQueryChange: (query: String) -> Unit,
    onSearch: () -> Unit,
    onProductClick: (productId: Int) -> Unit,
    onSortSelect: (sortType: SortType) -> Unit,
    onCategorySelect: (category: Category) -> Unit,
    onApplyFilters: () -> Unit,
    onResetFilters: () -> Unit,
    onDismissFilters: () -> Unit,
    onOpenFilters: () -> Unit,
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
        SearchSection(
            content = content,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            onFiltersClick = onOpenFilters
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Total items(${content.totalProducts})",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ProductList(lazyListState, content, onProductClick = onProductClick)
        FiltersBottomSheet(
            filterState = content.filterState,
            onSortSelect = onSortSelect,
            onCategorySelect = onCategorySelect,
            onApplyFilters = onApplyFilters,
            onResetFilters = onResetFilters,
            onDismissFilters = onDismissFilters,
        )
    }
}

@Composable
private fun SearchSection(
    content: ProductListState.Content,
    onQueryChange: (query: String) -> Unit,
    onSearch: () -> Unit,
    onFiltersClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        SearchTextField(
            text = content.query,
            onValueChange = onQueryChange,
            onSearch = onSearch,
            errorText = content.queryError,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f, fill = false)
        )
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = onFiltersClick) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "Filter and Sort"
            )
        }
    }
}

@Composable
private fun ProductList(
    lazyListState: LazyListState,
    content: ProductListState.Content,
    onProductClick: (productId: Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize()
        ) {
            when (content.searchState) {
                is SearchState.Loaded -> items(content.searchState.searchedProducts) { product ->
                    ProductListItem(product = product, onClick = { onProductClick(product.id) })
                    Spacer(modifier = Modifier.height(16.dp))
                }

                is SearchState.NoResult -> item { Text(text = "No result.") }
                is SearchState.Loading -> item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }

                is SearchState.Empty -> items(content.products) { product ->
                    ProductListItem(product = product, onClick = { onProductClick(product.id) })
                    Spacer(modifier = Modifier.height(16.dp))
                }

            }
        }
        if (content.pageLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}
