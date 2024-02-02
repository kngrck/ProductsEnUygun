package com.example.productsenuygun.data.repository

import com.example.productsenuygun.data.api.ProductApi
import com.example.productsenuygun.domain.mapper.toUiModel
import com.example.productsenuygun.domain.model.PaginatedProducts
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

    companion object {
        private const val LIMIT = 8
    }
}