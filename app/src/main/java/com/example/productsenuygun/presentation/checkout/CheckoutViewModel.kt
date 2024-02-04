package com.example.productsenuygun.presentation.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsenuygun.domain.repository.CartRepository
import com.example.productsenuygun.domain.usecase.calculateDiscount
import com.example.productsenuygun.domain.usecase.calculateTotal
import com.example.productsenuygun.domain.usecase.calculateTotalPrice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _viewState: MutableStateFlow<CheckoutState> =
        MutableStateFlow(CheckoutState.Loading)
    val viewState = _viewState.asStateFlow()

    fun initCheckout() {
        viewModelScope.launch(Dispatchers.IO) {
            val products = cartRepository.getCartProducts()
            _viewState.update {
                CheckoutState.Content(
                    products = products,
                    totalDiscount = products.calculateDiscount(),
                    total = products.calculateTotal(),
                    totalPrice = products.calculateTotalPrice()
                )
            }
        }
    }

    fun onProceed() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                cartRepository.emptyCart()
            }.onFailure {
                Log.e("Error", "Cart $it")
            }
        }
    }
}