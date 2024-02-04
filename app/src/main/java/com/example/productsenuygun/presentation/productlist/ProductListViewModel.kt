package com.example.productsenuygun.presentation.productlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productsenuygun.DispatcherProvider
import com.example.productsenuygun.domain.model.PaginatedProducts
import com.example.productsenuygun.domain.model.filter.Category
import com.example.productsenuygun.domain.model.filter.SortType
import com.example.productsenuygun.domain.model.filter.defaultCategory
import com.example.productsenuygun.domain.repository.CartRepository
import com.example.productsenuygun.domain.repository.ProductRepository
import com.example.productsenuygun.domain.usecase.SortProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val cartRepository: CartRepository,
    private val sortProductsUseCase: SortProductsUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _productListState: MutableStateFlow<ProductListState> =
        MutableStateFlow(ProductListState.Loading)
    val state: StateFlow<ProductListState> = _productListState.asStateFlow()

    fun initProductList() {
        viewModelScope.launch(dispatchers.io) {
            cartRepository.initCart()
            val categoriesDeferred = async {
                runCatching {
                    repository.getCategories()
                }.onFailure {
                    Log.e(
                        "Error",
                        "Could not fetch categories $it"
                    )
                }
            }
            val productsDeferred = async {
                runCatching {
                    getProducts()
                }.onFailure {
                    Log.e(
                        "Error",
                        "Could not fetch products $it"
                    )
                    _productListState.update {
                        ProductListState.Error("Something went wrong!")
                    }
                }
            }
            val categories = categoriesDeferred.await().getOrNull()
            val products = productsDeferred.await().getOrNull() ?: return@launch
            setProducts(products)
            categories?.let { setCategories(it) }
        }
    }

    fun loadMoreProducts() {
        val state = currentContentState() ?: return
        if (state.pageLoading || state.isLastPage) return

        showPaginationLoader()

        viewModelScope.launch(dispatchers.io) {
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
        if (query.length < 3) {
            _productListState.update {
                state.copy(queryError = "At least 3 characters.")
            }
            return
        }

        showSearchLoader()
        viewModelScope.launch(dispatchers.io) {
            runCatching {
                val searchedProducts = repository.searchProducts(query)
                val searchState =
                    if (searchedProducts.isEmpty()) SearchState.NoResult else SearchState.Loaded(
                        searchedProducts
                    )
                _productListState.update {
                    state.copy(searchState = searchState, isLastPage = true, queryError = "")
                }
            }.onFailure {
                Log.e("Error", it.message.orEmpty())
            }
        }
    }

    fun onSortSelect(sortType: SortType) {
        val currentState = currentContentState() ?: return
        val filterState = currentState.filterState
        _productListState.update {
            currentState.copy(filterState = filterState.copy(selectedSort = sortType))
        }
    }

    fun onCategorySelect(category: Category) {
        val currentState = currentContentState() ?: return
        val filterState = currentState.filterState
        _productListState.update {
            currentState.copy(filterState = filterState.copy(selectedCategory = category))
        }
    }

    fun onApplyFilters() {
        val currentState = currentContentState() ?: return
        val filterState = currentState.filterState

        viewModelScope.launch(dispatchers.io) {
            runCatching {
                _productListState.update {
                    currentState.copy(
                        filterState = filterState.copy(
                            isApplied = true,
                            isBottomSheetOpen = false,
                        ),
                        searchState = SearchState.Empty,
                        queryError = "",
                        query = ""
                    )
                }
                val products = getProducts()
                setProducts(products)
            }.onFailure {
                Log.e(
                    "Error",
                    "Could not fetch products"
                )
                _productListState.update {
                    ProductListState.Error("Something went wrong!")
                }
            }
        }
    }

    fun onResetFilters() {
        val currentState = currentContentState() ?: return
        val filterState = currentState.filterState

        if (filterState.isApplied) {
            viewModelScope.launch(dispatchers.io) {
                _productListState.update {
                    currentState.copy(
                        filterState = filterState.copy(
                            selectedCategory = defaultCategory(),
                            selectedSort = SortType.DEFAULT,
                            isApplied = false,
                            isBottomSheetOpen = false,
                        ),
                        searchState = SearchState.Empty,
                        queryError = "",
                        query = ""
                    )
                }

                val products = getProducts()
                setProducts(products)
            }
        } else {
            _productListState.update {
                currentState.copy(
                    filterState = filterState.copy(
                        selectedCategory = defaultCategory(),
                        selectedSort = SortType.DEFAULT,
                        isBottomSheetOpen = false
                    )
                )
            }
        }
    }

    fun onOpenFilters() {
        val currentState = currentContentState() ?: return
        val filterState = currentState.filterState

        _productListState.update {
            currentState.copy(
                filterState = filterState.copy(
                    isBottomSheetOpen = true
                )
            )
        }
    }

    fun onDismissFilters() {
        val currentState = currentContentState() ?: return
        val filterState = currentState.filterState

        _productListState.update {
            currentState.copy(
                filterState = filterState.copy(
                    isBottomSheetOpen = false,
                    selectedSort = if (filterState.isApplied) filterState.selectedSort else SortType.DEFAULT,
                    selectedCategory = if (filterState.isApplied) filterState.selectedCategory else defaultCategory()
                )
            )
        }
    }

    private suspend fun getProducts(): PaginatedProducts {
        val currentContentState = currentContentState()
        val isSortDefault =
            currentContentState == null || currentContentState.filterState.selectedSort == SortType.DEFAULT
        val category = currentContentState?.filterState?.selectedCategory

        return repository.getProducts(
            page = 1,
            usePagination = isSortDefault,
            category = category ?: defaultCategory()
        )
    }

    private fun setProducts(paginatedProducts: PaginatedProducts) {
        val currentContentState = currentContentState()

        _productListState.update {
            currentContentState?.let {
                it.copy(
                    products = sortProductsUseCase.run(
                        SortProductsUseCase.Params(
                            products = paginatedProducts.products,
                            it.filterState.selectedSort
                        )
                    ),
                    isLastPage = paginatedProducts.isLastPage,
                    totalProducts = paginatedProducts.total
                )
            } ?: ProductListState.Content(
                products = paginatedProducts.products,
                isLastPage = paginatedProducts.isLastPage,
                totalProducts = paginatedProducts.total
            )
        }
    }

    private fun setCategories(categories: List<Category>) {
        val currentContentState = currentContentState() ?: return
        val filterState = currentContentState.filterState

        _productListState.update {
            currentContentState.copy(filterState = filterState.copy(categories = categories))
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