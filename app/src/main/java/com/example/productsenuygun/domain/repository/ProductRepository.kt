package com.example.productsenuygun.domain.repository

import com.example.productsenuygun.domain.model.PaginatedProducts
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.domain.model.filter.Category
import com.example.productsenuygun.domain.model.filter.defaultCategory

interface ProductRepository {

    suspend fun getProducts(
        page: Int,
        usePagination: Boolean = true,
        category: Category = defaultCategory()
    ): PaginatedProducts

    suspend fun getProductById(id: Int): ProductUiModel

    suspend fun searchProducts(query: String): List<ProductUiModel>

    suspend fun getCategories(): List<Category>

    suspend fun getFavorites(): List<ProductUiModel>

    suspend fun addFavorite(product: ProductUiModel)

    suspend fun deleteFavorite(id: Int)
}