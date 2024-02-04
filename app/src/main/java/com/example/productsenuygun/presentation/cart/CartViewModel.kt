package com.example.productsenuygun.presentation.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.domain.repository.CartRepository
import com.example.productsenuygun.domain.usecase.decreaseQuantityBy
import com.example.productsenuygun.domain.usecase.increaseQuantityById
import com.example.productsenuygun.domain.usecase.removeProductById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {

    private val _viewState: MutableStateFlow<CartState> = MutableStateFlow(CartState.Loading)
    val viewState = _viewState.asStateFlow()

    fun initCart() {
        getCartProducts()
    }

    fun onIncreaseQuantity(product: ProductUiModel) {
        val currentState = currentContentState() ?: return
        val products = currentState.products

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.increaseQuantityById(product)
                val updatedProducts = products.increaseQuantityById(product.id)
                setProducts(updatedProducts)
            }.onFailure {
                Log.e("Error", "Cart $it")
            }
        }
    }

    fun onDecreaseQuantity(product: ProductUiModel) {
        val currentState = currentContentState() ?: return
        val products = currentState.products

        if (product.quantity == 1) {
            onRemoveFromCart(product)
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.decreaseQuantityById(product.id)
                val updatedProducts = products.decreaseQuantityBy(product.id)
                setProducts(updatedProducts)
            }.onFailure {
                Log.e("Error", "Cart $it")
            }
        }
    }

    fun onRemoveFromCart(product: ProductUiModel) {
        val currentState = currentContentState() ?: return
        val products = currentState.products

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.removeProductById(product.id)
                val updatedProducts = products.removeProductById(product.id)
                setProducts(updatedProducts)
            }.onFailure {
                Log.e("Error", "Cart $it")
            }
        }
    }

    fun onPurchase() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.emptyCart()
                setProducts(emptyList())
            }.onFailure {
                Log.e("Error", "Cart $it")
            }
        }
    }

    private fun getCartProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val products = repository.getCartProducts()
                setProducts(products)
            }.onFailure {
                Log.e("Error", "Cart $it")
                _viewState.update {
                    CartState.Error("Something went wrong!")
                }
            }
        }
    }

    private fun setProducts(products: List<ProductUiModel>) {
        val currentState = currentContentState()

        _viewState.update {
            currentState?.copy(
                products = products,
                total = products.calculateTotal(),
                totalPrice = products.calculateTotalPrice(),
                totalDiscount = products.calculateDiscount()
            ) ?: CartState.Content(
                products = products,
                total = products.calculateTotal(),
                totalPrice = products.calculateTotalPrice(),
                totalDiscount = products.calculateDiscount()
            )
        }
    }

    private fun List<ProductUiModel>.calculateTotalPrice() = sumOf { it.price * it.quantity }

    private fun List<ProductUiModel>.calculateDiscount() =
        calculateTotalPrice() - sumOf { it.discountedPrice * it.quantity }

    private fun List<ProductUiModel>.calculateTotal() =
        sumOf { it.discountedPrice * it.quantity }

    private fun currentContentState() = _viewState.value as? CartState.Content
}