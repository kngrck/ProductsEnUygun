package com.example.productsenuygun.data.repository

import com.example.productsenuygun.data.api.ProductApi
import com.example.productsenuygun.domain.mapper.toUiModel
import com.example.productsenuygun.domain.model.PaginatedProducts
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.domain.model.filter.Category
import com.example.productsenuygun.domain.model.filter.defaultCategory
import com.example.productsenuygun.domain.repository.ProductRepository
import java.util.Locale
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi
) : ProductRepository {

    override suspend fun getProducts(
        page: Int,
        usePagination: Boolean,
        category: Category
    ): PaginatedProducts {
        val hasCategory = category != defaultCategory()
        val isPaginationEnabled = usePagination && !hasCategory
        val limit = if (isPaginationEnabled) LIMIT else 0
        val skip = (page - 1) * limit
        val response =
            if (hasCategory) {
                api.getProductsWithCategory(
                    category = category.value,
                    skip = skip,
                    limit = limit,
                )
            } else {
                api.getProducts(skip, limit)
            }
        val products = response.products.map { it.toUiModel() }
        val isLastPage = if (isPaginationEnabled) {
            page.toFloat() > response.total.toFloat().div(LIMIT.toFloat())
        } else {
            true
        }

        return PaginatedProducts(
            isLastPage = isLastPage,
            products = products,
            total = response.total
        )
    }

    override suspend fun getProductById(id: Int): ProductUiModel {
        return api.getProductById(id).toUiModel()
    }

    override suspend fun searchProducts(query: String): List<ProductUiModel> {
        return api.searchProducts(query).products.map { it.toUiModel() }
    }

    override suspend fun getCategories(): List<Category> {
        return api.getCategories().map { category ->
            Category(
                name = category.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                },
                value = category
            )
        }
    }

    companion object {
        private const val LIMIT = 8
    }
}