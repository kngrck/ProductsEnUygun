package com.example.productsenuygun.presentation.favorites

import com.example.productsenuygun.domain.model.ProductUiModel

sealed interface FavoritesState {

    data object Loading : FavoritesState

    data class Content(val products: List<ProductUiModel>) : FavoritesState

    data class Error(val message: String) : FavoritesState
}