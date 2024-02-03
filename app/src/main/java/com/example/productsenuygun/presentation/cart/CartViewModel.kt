package com.example.productsenuygun.presentation.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsenuygun.domain.model.CartProductUiModel
import com.example.productsenuygun.domain.repository.CartRepository
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

    fun onIncreaseQuantity(cartProduct: CartProductUiModel) {
        val currentState = currentContentState() ?: return
        val products = currentState.products

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.increaseQuantityById(cartProduct)
                val updatedProducts = products.increaseQuantityById(cartProduct.id)
                setProducts(updatedProducts)
            }.onFailure {
                Log.e("Error", "Cart $it")
            }
        }
    }

    fun onDecreaseQuantity(cartProduct: CartProductUiModel) {
        val currentState = currentContentState() ?: return
        val products = currentState.products

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.decreaseQuantityById(cartProduct.id)
                val updatedProducts = products.decreaseQuantityBy(cartProduct.id)
                setProducts(updatedProducts)
            }.onFailure {
                Log.e("Error", "Cart $it")
            }
        }
    }

    fun onRemoveFromCart(cartProduct: CartProductUiModel) {
        val currentState = currentContentState() ?: return
        val products = currentState.products

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.removeProductById(cartProduct.id)
                val updatedProducts = products.removeProductById(cartProduct.id)
                setProducts(updatedProducts)
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

    private fun setProducts(products: List<CartProductUiModel>) {
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

    private fun List<CartProductUiModel>.increaseQuantityById(id: Int): List<CartProductUiModel> {
        return map {
            if (it.id == id) {
                it.copy(quantity = it.quantity + 1)
            } else it
        }
    }

    private fun List<CartProductUiModel>.decreaseQuantityBy(id: Int): List<CartProductUiModel> {
        return mapNotNull {
            if (it.id == id) {
                if (it.quantity == 1) null else it.copy(quantity = it.quantity - 1)
            } else it
        }
    }

    private fun List<CartProductUiModel>.removeProductById(id: Int): List<CartProductUiModel> {
        return filter { it.id != id }
    }

    private fun List<CartProductUiModel>.calculateTotalPrice() = sumOf { it.price * it.quantity }

    private fun List<CartProductUiModel>.calculateDiscount() =
        calculateTotalPrice() - sumOf { it.discountedPrice * it.quantity }

    private fun List<CartProductUiModel>.calculateTotal() =
        sumOf { it.discountedPrice * it.quantity }

    private fun currentContentState() = _viewState.value as? CartState.Content
}