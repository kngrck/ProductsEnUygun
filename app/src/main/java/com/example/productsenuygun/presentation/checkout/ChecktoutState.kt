package com.example.productsenuygun.presentation.checkout

import com.example.productsenuygun.domain.model.ProductUiModel

sealed interface CheckoutState {

    data object Loading : CheckoutState

    data class Content(
        val products: List<ProductUiModel>,
        val totalPrice: Int,
        val totalDiscount: Int,
        val total: Int
    ) : CheckoutState

    data class Error(val message: String) : CheckoutState
}