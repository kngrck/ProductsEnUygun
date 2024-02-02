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

        showPaginationLoader()

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

    fun onQueryChange(query: String) {
        val state = currentContentState() ?: return
        val updateSearchState =
            if (query.isEmpty()) SearchState.Empty else state.searchState

        _productListState.update {
            state.copy(
                query = query,
                searchState = updateSearchState,
                isLastPage = query.isNotEmpty(),
                queryError = if (query.isEmpty()) "" else state.queryError
            )
        }
    }

    fun onSearch() {
        val state = currentContentState() ?: return
        val query = state.query
        if (query.length < 2) {
            _productListState.update {
                state.copy(queryError = "At least 3 characters.")
            }
            return
        }

        showSearchLoader()
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val searchedProducts = repository.searchProducts(query)
                val searchState =
                    if (searchedProducts.isEmpty()) SearchState.NoResult else SearchState.Loaded(
                        searchedProducts
                    )
                _productListState.update {
                    state.copy(searchState = searchState, queryError = "")
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

    private fun showPaginationLoader() {
        currentContentState()?.let { content ->
            _productListState.update {
                content.copy(pageLoading = true)
            }
        }
    }

    private fun showSearchLoader() {
        currentContentState()?.let { content ->
            _productListState.update {
                content.copy(searchState = SearchState.Loading)
            }
        }
    }

    private fun currentContentState(): ProductListState.Content? {
        return _productListState.value as? ProductListState.Content
    }
}