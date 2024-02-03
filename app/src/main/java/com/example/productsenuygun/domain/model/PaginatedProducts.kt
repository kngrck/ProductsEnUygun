package com.example.productsenuygun.domain.model

data class PaginatedProducts(
    val isLastPage: Boolean,
    val products: List<ProductUiModel>,
    val total: Int
)
