package com.example.productsenuygun.presentation.productlist

import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.domain.model.filter.Category
import com.example.productsenuygun.domain.model.filter.SortType
import com.example.productsenuygun.domain.model.filter.defaultCategory

sealed interface ProductListState {

    data object Loading : ProductListState

    data class Content(
        val products: List<ProductUiModel>,
        val isLastPage: Boolean,
        val totalProducts: Int,
        val currentPage: Int = 1,
        val pageLoading: Boolean = false,
        val query: String = "",
        val queryError: String = "",
        val searchState: SearchState = SearchState.Empty,
        val filterState: FilterState = FilterState(),
    ) : ProductListState

    data class Error(val message: String) : ProductListState
}

sealed interface SearchState {
    data object Loading : SearchState

    data object Empty : SearchState

    data object NoResult : SearchState

    data class Loaded(val searchedProducts: List<ProductUiModel>) : SearchState
}

data class FilterState(
    val selectedSort: SortType = SortType.DEFAULT,
    val selectedCategory: Category = defaultCategory(),
    val categories: List<Category> = emptyList(),
    val isApplied: Boolean = false,
    val isBottomSheetOpen: Boolean = false,
)