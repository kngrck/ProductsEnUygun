package com.example.productsenuygun.presentation.productdetail

import com.example.productsenuygun.domain.model.ProductUiModel

sealed interface ProductDetailState {

    data object Loading : ProductDetailState

    data class Content(val product: ProductUiModel) : ProductDetailState

    data class Error(val message: String) : ProductDetailState
}