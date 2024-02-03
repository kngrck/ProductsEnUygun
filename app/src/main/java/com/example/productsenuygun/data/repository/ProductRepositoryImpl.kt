package com.example.productsenuygun.data.repository

import com.example.productsenuygun.data.api.ProductApi
import com.example.productsenuygun.domain.mapper.toUiModel
import com.example.productsenuygun.domain.model.PaginatedProducts
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getProducts(page: Int): PaginatedProducts {
        val skip = (page - 1) * LIMIT
        val response = api.getProducts(skip, LIMIT)
        val products = api.getProducts(skip, LIMIT).products.map { it.toUiModel() }
        val isLastPage = page.toFloat() > response.total.toFloat().div(LIMIT.toFloat())

        return PaginatedProducts(isLastPage = isLastPage, products = products)
    }

    override suspend fun getProductById(id: Int): ProductUiModel {
        return api.getProductById(id).toUiModel()
    }

    override suspend fun searchProducts(query: String): List<ProductUiModel> {
        return api.searchProducts(query).products.map { it.toUiModel() }
    }

    companion object {
        private const val LIMIT = 8
    }
}