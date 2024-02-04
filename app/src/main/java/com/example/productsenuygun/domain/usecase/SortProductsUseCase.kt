package com.example.productsenuygun.domain.usecase

import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.domain.model.filter.SortType
import javax.inject.Inject

class SortProductsUseCase @Inject constructor() {

    fun run(params: Params): List<ProductUiModel> {
        return when (params.sortType) {
            SortType.DEFAULT -> params.products
            SortType.ALPHABETICALLY_A_Z -> params.products.sortedBy { it.title }
            SortType.ALPHABETICALLY_Z_A -> params.products.sortedByDescending { it.title }
            SortType.PRICE_LOW_TO_HIGH -> params.products.sortedBy { it.discountedPrice }
            SortType.PRICE_HIGH_TO_LOW -> params.products.sortedByDescending { it.discountedPrice }
        }
    }

    data class Params(
        val products: List<ProductUiModel>,
        val sortType: SortType
    )
}