package com.example.productsenuygun.domain.mapper

import com.example.productsenuygun.data.local.entity.CartProduct
import com.example.productsenuygun.domain.model.CartProductUiModel
import com.example.productsenuygun.domain.model.ProductUiModel

fun CartProduct.toUiModel() = CartProductUiModel(
    id = id,
    name = name,
    quantity = quantity,
    price = price,
    discountedPrice = discountedPrice,
    image = image
)

fun CartProductUiModel.toCartProduct() = CartProduct(
    id = id,
    name = name,
    quantity = quantity,
    price = price,
    discountedPrice = discountedPrice,
    image = image
)

fun ProductUiModel.toCartProductUiModel() = CartProductUiModel(
    id = id,
    name = title,
    quantity = 0,
    price = price,
    discountedPrice = discountPrice,
    image = images.firstOrNull().orEmpty()
)