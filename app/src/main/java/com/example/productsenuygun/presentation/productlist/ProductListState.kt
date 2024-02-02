package com.example.productsenuygun.presentation.productlist

import com.example.productsenuygun.domain.model.ProductUiModel

sealed interface ProductListState {

    data object Loading : ProductListState

    data class Content(
        val products: List<ProductUiModel>,
        val isLastPage: Boolean,
        val currentPage: Int = 1,
        val pageLoading: Boolean = false,
        val query: String = "",
        val queryError: String = "",
        val searchState: SearchState = SearchState.Empty,
    ) : ProductListState

    data class Error(val message: String) : ProductListState
}

sealed interface SearchState {
    data object Loading : SearchState

    data object Empty : SearchState

    data object NoResult : SearchState

    data class Loaded(val searchedProducts: List<ProductUiModel>) : SearchState
}