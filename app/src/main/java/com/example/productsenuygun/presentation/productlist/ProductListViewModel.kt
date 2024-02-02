package com.example.productsenuygun.presentation.productlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsenuygun.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    fun loadMoreProducts() {
        val state = currentContentState() ?: return
        if (state.pageLoading || state.isLastPage) return

        setPaginationLoader(true)

        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val nextPage = state.currentPage + 1
                val paginatedProducts = repository.getProducts(nextPage)
                val currentProducts = state.products.toMutableList()
                currentProducts.addAll(paginatedProducts.products)

                _productListState.update {
                    state.copy(
                        products = currentProducts,
                        pageLoading = false,
                        isLastPage = paginatedProducts.isLastPage,
                        currentPage = nextPage
                    )
                }
            }.onFailure {
                Log.e("Error", it.message.orEmpty())
            }
        }
    }

    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val paginatedProducts = repository.getProducts(page = 1)
                _productListState.update {
                    ProductListState.Content(
                        products = paginatedProducts.products,
                        isLastPage = paginatedProducts.isLastPage
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

    private fun setPaginationLoader(isLoading: Boolean) {
        currentContentState()?.let { content ->
            _productListState.update {
                content.copy(pageLoading = isLoading)
            }
        }
    }

    private fun currentContentState(): ProductListState.Content? {
        return _productListState.value as? ProductListState.Content
    }
}