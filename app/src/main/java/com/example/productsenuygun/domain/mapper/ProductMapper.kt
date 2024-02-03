package com.example.productsenuygun.domain.mapper

import com.example.productsenuygun.data.model.Product
import com.example.productsenuygun.domain.model.ProductUiModel

fun Product.toUiModel() = ProductUiModel(
    id = id,
    brand = brand,
    category = category,
    description = description,
    discountPercentage = discountPercentage,
    images = images,
    price = price,
    rating = rating,
    stock = stock,
    thumbnail = thumbnail,
    title = title,
    discountPrice = if (discountPercentage == 0.0) {
        0
    } else {
        (price.toDouble() * (100.0 - discountPercentage) / 100.0).toInt()
    }
)