package com.example.productsenuygun.domain.model

data class ProductUiModel(
    val id: Int,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    val images: List<String>,
    val price: Int,
    val discountedPrice: Int,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val title: String,
    val isFavorite: Boolean = false,
    val quantity: Int = 0,
)

