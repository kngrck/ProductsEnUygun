package com.example.productsenuygun.domain.repository

import com.example.productsenuygun.domain.model.CartProductUiModel

interface CartRepository {

    suspend fun getCartProducts(): List<CartProductUiModel>

    suspend fun getCartProductById(id: Int): CartProductUiModel?

    suspend fun increaseQuantityById(cartProductUiModel: CartProductUiModel)

    suspend fun decreaseQuantityById(id: Int)

    suspend fun removeProductById(id: Int)
}