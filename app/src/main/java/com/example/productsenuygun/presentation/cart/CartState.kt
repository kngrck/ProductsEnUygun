package com.example.productsenuygun.presentation.cart

import com.example.productsenuygun.domain.model.ProductUiModel

sealed interface CartState {

    data object Loading : CartState

    data class Content(
        val products: List<ProductUiModel>,
        val totalPrice: Int,
        val totalDiscount: Int,
        val total: Int
    ) : CartState

    data class Error(val message: String) : CartState
}