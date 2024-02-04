package com.example.productsenuygun.domain.usecase

import com.example.productsenuygun.domain.model.ProductUiModel

fun List<ProductUiModel>.increaseQuantityById(id: Int): List<ProductUiModel> {
    return map {
        if (it.id == id) {
            it.copy(quantity = it.quantity + 1)
        } else it
    }
}

fun List<ProductUiModel>.decreaseQuantityBy(id: Int): List<ProductUiModel> {

    return map {
        if (it.id == id) {
            if (it.quantity > 0) it.copy(quantity = it.quantity - 1) else it
        } else it
    }
}

fun List<ProductUiModel>.removeProductById(id: Int): List<ProductUiModel> {
    return filter { it.id != id }
}

fun List<ProductUiModel>.calculateTotalPrice() = sumOf { it.price * it.quantity }

fun List<ProductUiModel>.calculateDiscount() =
    calculateTotalPrice() - sumOf { it.discountedPrice * it.quantity }

fun List<ProductUiModel>.calculateTotal() =
    sumOf { it.discountedPrice * it.quantity }