package com.example.productsenuygun.domain.repository

import com.example.productsenuygun.domain.model.ProductUiModel

interface ProductRepository {

    suspend fun getProducts(): List<ProductUiModel>
}