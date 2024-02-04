package com.example.productsenuygun.data.repository

import com.example.productsenuygun.data.api.ProductApi
import com.example.productsenuygun.data.local.AppDatabase
import com.example.productsenuygun.domain.mapper.toFavoriteProduct
import com.example.productsenuygun.domain.mapper.toUiModel
import com.example.productsenuygun.domain.model.PaginatedProducts
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.domain.model.filter.Category
import com.example.productsenuygun.domain.model.filter.defaultCategory
import com.example.productsenuygun.domain.repository.ProductRepository
import java.util.Locale
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: ProductApi,
    private val localDatabase: AppDatabase
) : ProductRepository {
    private val favoriteDao = localDatabase.favoriteDao()
    private val cartDao = localDatabase.cartDao()

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
        val cartProduct = cartDao.getCartProductById(id)
        val favoriteProduct = favoriteDao.getFavoriteById(id)

        return api.getProductById(id).toUiModel().copy(
            quantity = cartProduct?.quantity ?: 0,
            isFavorite = favoriteProduct != null
        )
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

    override suspend fun getFavorites(): List<ProductUiModel> {
        return favoriteDao.getAllFavorites().map {
            it.toUiModel().copy(
                quantity = cartDao.getCartProductById(it.id)?.quantity ?: 0
            )
        }
    }

    override suspend fun addFavorite(product: ProductUiModel) {
        favoriteDao.addFavorite(product.toFavoriteProduct())
    }

    override suspend fun deleteFavorite(id: Int) {
        favoriteDao.deleteFavoriteById(id)
    }

    companion object {
        private const val LIMIT = 8
    }
}