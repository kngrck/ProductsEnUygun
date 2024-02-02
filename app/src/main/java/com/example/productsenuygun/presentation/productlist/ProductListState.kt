package com.example.productsenuygun.presentation.productlist

import com.example.productsenuygun.domain.model.ProductUiModel

sealed interface ProductListState {

    data object Loading : ProductListState

    data class Content(
        val products: List<ProductUiModel>,
        val isLastPage: Boolean,
        val currentPage: Int = 1,
        val pageLoading: Boolean = false,
    ) : ProductListState

    data class Error(val message: String) : ProductListState
}