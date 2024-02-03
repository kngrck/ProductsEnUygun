package com.example.productsenuygun.presentation.productdetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsenuygun.domain.repository.ProductRepository
import com.example.productsenuygun.presentation.navigation.Arguments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ProductRepository
) : ViewModel() {

    private val productId: Int = savedStateHandle[Arguments.PRODUCT_ID.name]!!
    private val _viewState: MutableStateFlow<ProductDetailState> =
        MutableStateFlow(ProductDetailState.Loading)
    val viewState: StateFlow<ProductDetailState> = _viewState.asStateFlow()

    init {
        getProduct()
    }

    fun onFavoriteClick() {
        val currentState = currentContentState() ?: return
        val product = currentState.product
        _viewState.update {
            currentState.copy(product = product.copy(isFavorite = !product.isFavorite))
        }
        //TODO Add call
    }

    private fun getProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val product = repository.getProductById(productId)
                _viewState.update {
                    ProductDetailState.Content(product = product)
                }
            }.onFailure {
                Log.e("Error", it.message.orEmpty())
                _viewState.update {
                    ProductDetailState.Error("Something went wrong!")
                }
            }
        }
    }

    private fun currentContentState() = _viewState.value as? ProductDetailState.Content
}