package com.example.productsenuygun.domain.repository

import com.example.productsenuygun.domain.model.ProductUiModel
import kotlinx.coroutines.flow.SharedFlow

interface CartRepository {

    val cartTotalItems: SharedFlow<Int>

    suspend fun getCartProducts(): List<ProductUiModel>

    suspend fun getCartProductById(id: Int): ProductUiModel?

    suspend fun increaseQuantityById(productUiModel: ProductUiModel)

    suspend fun decreaseQuantityById(id: Int)

    suspend fun removeProductById(id: Int)

    suspend fun initCart()
}