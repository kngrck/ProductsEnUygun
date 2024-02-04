package com.example.productsenuygun

import com.example.productsenuygun.domain.model.ProductUiModel

object DataGenerator {

    fun getProductUiModel(
        id: Int = 0,
        price: Int = 500,
        discountedPrice: Int = 450,
        discountPercentage: Double = 10.0,
        isFavorite: Boolean = false,
        quantity: Int = 0
    ) = ProductUiModel(
        id = id,
        category = "Electronics",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        discountPercentage = discountPercentage,
        images = listOf("image1.jpg", "image2.jpg", "image3.jpg"),
        price = price,
        discountedPrice = discountedPrice,
        rating = 4.5,
        stock = 20,
        thumbnail = "thumbnail1.jpg",
        title = "Smartphone XYZ",
        isFavorite = isFavorite,
        quantity = quantity
    )
}