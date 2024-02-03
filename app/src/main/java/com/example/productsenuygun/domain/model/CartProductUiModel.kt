package com.example.productsenuygun.domain.model

data class CartProductUiModel(
    val id: Int,
    val name: String,
    val quantity: Int,
    val price: Int,
    val discountedPrice: Int
)
