package com.example.productsenuygun.data.repository

import com.example.productsenuygun.data.api.ProductApi
import com.example.productsenuygun.domain.mapper.toUiModel
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
) : ProductRepository {
    override suspend fun getProducts(): List<ProductUiModel> {
        return api.getProducts().products.map { it.toUiModel() }
    }
}