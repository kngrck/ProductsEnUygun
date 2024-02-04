package com.example.productsenuygun.presentation.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.domain.repository.CartRepository
import com.example.productsenuygun.domain.repository.ProductRepository
import com.example.productsenuygun.domain.usecase.decreaseQuantityBy
import com.example.productsenuygun.domain.usecase.increaseQuantityById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    private val _viewState: MutableStateFlow<FavoritesState> =
        MutableStateFlow(FavoritesState.Loading)
    val viewState = _viewState.asStateFlow()

    fun initFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val products = productRepository.getFavorites()

                _viewState.update {
                    FavoritesState.Content(products)
                }
            }.onFailure {
                Log.e("Error", "Favorites $it")
                _viewState.update {
                    FavoritesState.Error("Something went wrong!")
                }
            }
        }
    }

    fun removeFavorite(id: Int) {
        val currentState = _viewState.value as? FavoritesState.Content ?: return

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                productRepository.deleteFavorite(id)

                _viewState.update {
                    currentState.copy(
                        products = currentState.products.filter { it.id != id }
                    )
                }
            }.onFailure {
                Log.e("Error", "Favorites $it")
            }
        }
    }

    fun onIncreaseQuantity(product: ProductUiModel) {
        val currentState = currentContentState() ?: return
        val products = currentState.products

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                cartRepository.increaseQuantityById(product)
                val updatedProducts = products.increaseQuantityById(product.id)
                _viewState.update {
                    currentState.copy(products = updatedProducts)
                }
            }.onFailure {
                Log.e("Error", "Favorites $it")
            }
        }
    }

    fun onDecreaseQuantity(id: Int) {
        val currentState = currentContentState() ?: return
        val products = currentState.products

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                cartRepository.decreaseQuantityById(id)
                val updatedProducts = products.decreaseQuantityBy(id)
                _viewState.update {
                    currentState.copy(products = updatedProducts)
                }
            }.onFailure {
                Log.e("Error", "Favorites $it")
            }
        }
    }

    private fun currentContentState() = _viewState.value as? FavoritesState.Content
}