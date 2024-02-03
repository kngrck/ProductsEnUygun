package com.example.productsenuygun.domain.repository

import com.example.productsenuygun.domain.model.CartProductUiModel
import com.example.productsenuygun.domain.model.ProductUiModel

interface CartRepository {

    suspend fun getCartProducts(): List<CartProductUiModel>

    suspend fun getCartProductById(id: Int): CartProductUiModel?

    suspend fun increaseQuantityById(productUiModel: ProductUiModel)

    suspend fun decreaseQuantityById(id: Int)

}