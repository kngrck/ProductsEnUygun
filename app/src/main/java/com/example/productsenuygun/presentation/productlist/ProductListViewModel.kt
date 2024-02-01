package com.example.productsenuygun.presentation.productlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsenuygun.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _productListState: MutableStateFlow<ProductListState> =
        MutableStateFlow(ProductListState.Loading)
    val state: StateFlow<ProductListState> = _productListState.asStateFlow()

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            runCatching {
                val products = repository.getProducts()
                _productListState.update {
                    ProductListState.Content(
                        products = products
                    )
                }
            }.onFailure {
                Log.e("Error", it.message.orEmpty())
                _productListState.update {
                    ProductListState.Error("Something went wrong!")
                }
            }
        }
    }
}