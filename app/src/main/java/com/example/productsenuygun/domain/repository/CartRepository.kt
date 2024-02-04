package com.example.productsenuygun.domain.repository

import com.example.productsenuygun.domain.model.ProductUiModel

interface CartRepository {

    suspend fun getCartProducts(): List<ProductUiModel>

    suspend fun getCartProductById(id: Int): ProductUiModel?

    suspend fun increaseQuantityById(productUiModel: ProductUiModel)

    suspend fun decreaseQuantityById(id: Int)

    suspend fun removeProductById(id: Int)
}