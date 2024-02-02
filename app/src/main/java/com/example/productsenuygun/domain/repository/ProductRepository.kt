package com.example.productsenuygun.domain.repository

import com.example.productsenuygun.domain.model.PaginatedProducts

interface ProductRepository {

    suspend fun getProducts(page: Int): PaginatedProducts
}