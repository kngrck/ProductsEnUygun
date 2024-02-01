package com.example.productsenuygun.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponse(
    val products: List<Product>
)

@Serializable
data class Product(
    val id: Int,
    val brand: String,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    val images: List<String>,
    val price: Int,
    val rating: Double,
    val stock: Int,
    val thumbnail: String,
    val title: String
)