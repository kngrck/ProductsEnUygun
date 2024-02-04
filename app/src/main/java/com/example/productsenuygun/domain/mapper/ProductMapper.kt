package com.example.productsenuygun.domain.mapper

import com.example.productsenuygun.data.local.entity.CartProduct
import com.example.productsenuygun.data.local.entity.FavoriteProduct
import com.example.productsenuygun.data.model.Product
import com.example.productsenuygun.domain.model.ProductUiModel

fun Product.toUiModel() = ProductUiModel(
    id = id,
    category = category,
    description = description,
    discountPercentage = discountPercentage,
    images = images,
    price = price,
    rating = rating,
    stock = stock,
    thumbnail = thumbnail,
    title = title,
    discountedPrice = if (discountPercentage == 0.0) {
        0
    } else {
        (price.toDouble() * (100.0 - discountPercentage) / 100.0).toInt()
    }
)

fun FavoriteProduct.toUiModel() = ProductUiModel(
    id = id,
    title = name,
    price = price,
    discountedPrice = discountedPrice,
    isFavorite = true,
    images = listOf(image),
    category = "",
    description = "",
    discountPercentage = 0.0,
    rating = 0.0,
    stock = 0,
    thumbnail = ""
)

fun ProductUiModel.toFavoriteProduct() = FavoriteProduct(
    id = id,
    name = title,
    price = price,
    discountedPrice = discountedPrice,
    image = images.firstOrNull().orEmpty(),
)

fun CartProduct.toUiModel() = ProductUiModel(
    id = id,
    title = name,
    quantity = quantity,
    price = price,
    discountedPrice = discountedPrice,
    images = listOf(image),
    isFavorite = false,
    category = "",
    description = "",
    discountPercentage = 0.0,
    rating = 0.0,
    stock = 0,
    thumbnail = ""
)

fun ProductUiModel.toCartProduct() = CartProduct(
    id = id,
    name = title,
    quantity = quantity,
    price = price,
    discountedPrice = discountedPrice,
    image = images.firstOrNull().orEmpty()
)