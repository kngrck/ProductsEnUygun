package com.example.productsenuygun.presentation.productlist

import com.example.productsenuygun.domain.model.ProductUiModel

sealed interface ProductListState {

    data object Loading : ProductListState

    data class Content(val products: List<ProductUiModel>) : ProductListState

    data class Error(val message: String) : ProductListState
}