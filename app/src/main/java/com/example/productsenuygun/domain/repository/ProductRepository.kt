package com.example.productsenuygun.domain.repository

import com.example.productsenuygun.domain.model.PaginatedProducts
import com.example.productsenuygun.domain.model.ProductUiModel

interface ProductRepository {

    suspend fun getProducts(page: Int): PaginatedProducts

    suspend fun searchProducts(query: String): List<ProductUiModel>
}