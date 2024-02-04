package com.example.productsenuygun.presentation.productlist

import app.cash.turbine.test
import com.example.productsenuygun.DataGenerator.getProductUiModel
import com.example.productsenuygun.DispatcherProvider
import com.example.productsenuygun.TestCoroutineRule
import com.example.productsenuygun.TestDispatchers
import com.example.productsenuygun.domain.model.PaginatedProducts
import com.example.productsenuygun.domain.model.filter.Category
import com.example.productsenuygun.domain.model.filter.SortType
import com.example.productsenuygun.domain.model.filter.defaultCategory
import com.example.productsenuygun.domain.repository.CartRepository
import com.example.productsenuygun.domain.repository.ProductRepository
import com.example.productsenuygun.domain.usecase.SortProductsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class ProductListViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: ProductListViewModel
    private lateinit var repository: ProductRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var sortProductsUseCase: SortProductsUseCase
    private lateinit var testDispatcher: DispatcherProvider

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        cartRepository = mockk(relaxed = true)
        sortProductsUseCase = SortProductsUseCase()
        testDispatcher = TestDispatchers()
        viewModel =
            ProductListViewModel(repository, cartRepository, sortProductsUseCase, testDispatcher)
    }

    @Test
    fun `When initProductList Verify updates state correctly`() = runTest {
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )
        val products = listOf(
            getProductUiModel(id = 1),
            getProductUiModel(id = 2),
            getProductUiModel(id = 3),
        )
        val paginatedProducts = PaginatedProducts(
            products = products,
            isLastPage = false,
            total = 100
        )
        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(any()) } returns paginatedProducts

        viewModel.initProductList()
        viewModel.state.test {
            val loading = awaitItem()
            awaitItem() // set products
            val content = awaitItem() // set categories

            assertTrue(loading is ProductListState.Loading)
            assertTrue(content is ProductListState.Content)
            val state = content as ProductListState.Content

            assertEquals(false, state.isLastPage)
            assertEquals(1, state.currentPage)
            assertEquals(products, state.products)
            assertEquals(categories, state.filterState.categories)
        }
    }

    @Test
    fun `When initProductList if categories fails Verify state is content`() = runTest {
        val products = listOf(
            getProductUiModel(id = 1),
            getProductUiModel(id = 2),
            getProductUiModel(id = 3),
        )
        val paginatedProducts = PaginatedProducts(
            products = products,
            isLastPage = false,
            total = 100
        )
        coEvery { repository.getCategories() } throws Throwable()
        coEvery { repository.getProducts(any()) } returns paginatedProducts

        viewModel.initProductList()
        viewModel.state.test {
            val loading = awaitItem()
            val content = awaitItem()

            assertTrue(loading is ProductListState.Loading)
            assertTrue(content is ProductListState.Content)
            val state = content as ProductListState.Content

            assertEquals(false, state.isLastPage)
            assertEquals(1, state.currentPage)
            assertEquals(products, state.products)
            assertEquals(emptyList<Category>(), state.filterState.categories)
        }
    }

    @Test
    fun `When initProductList if products fails Verify state is error`() = runTest {
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )

        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(any()) } throws Throwable()

        viewModel.initProductList()
        viewModel.state.test {
            val loading = awaitItem()
            val error = awaitItem()

            assertTrue(loading is ProductListState.Loading)
            assertTrue(error is ProductListState.Error)
        }
    }

    @Test
    fun `When loadMoreProduct Verify state is updated`() = runTest {
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )
        val firstProducts = listOf(
            getProductUiModel(id = 1),
            getProductUiModel(id = 2),
            getProductUiModel(id = 3),
        )
        val secondProducts = listOf(
            getProductUiModel(id = 4),
            getProductUiModel(id = 5),
            getProductUiModel(id = 6),
        )
        val expectedProducts = buildList {
            addAll(firstProducts)
            addAll(secondProducts)
        }
        val firstPaginatedProducts = PaginatedProducts(
            products = firstProducts,
            isLastPage = false,
            total = 100
        )
        val secondPaginatedProducts = PaginatedProducts(
            products = secondProducts,
            isLastPage = true,
            total = 100
        )
        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(page = 1) } returns firstPaginatedProducts
        coEvery { repository.getProducts(page = 2) } returns secondPaginatedProducts

        viewModel.initProductList()
        advanceUntilIdle()
        viewModel.loadMoreProducts()
        advanceUntilIdle()

        coVerify { repository.getProducts(1) }
        coVerify { repository.getProducts(2) }

        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content

            assertEquals(true, state.isLastPage)
            assertEquals(2, state.currentPage)
            assertEquals(expectedProducts, state.products)
        }
    }

    @Test
    fun `When loadMoreProduct fails Verify state is not updated`() = runTest {
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )
        val firstProducts = listOf(
            getProductUiModel(id = 1),
            getProductUiModel(id = 2),
            getProductUiModel(id = 3),
        )

        val firstPaginatedProducts = PaginatedProducts(
            products = firstProducts,
            isLastPage = false,
            total = 100
        )

        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(page = 1) } returns firstPaginatedProducts
        coEvery { repository.getProducts(page = 2) } throws Throwable()

        viewModel.initProductList()
        advanceUntilIdle()
        viewModel.loadMoreProducts()
        advanceUntilIdle()

        coVerify { repository.getProducts(1) }
        coVerify { repository.getProducts(2) }

        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content

            assertEquals(false, state.isLastPage)
            assertEquals(1, state.currentPage)
            assertEquals(firstProducts, state.products)
        }
    }

    @Test
    fun `When query change Verify search query state is updated`() = runTest {
        val query = "Query"
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )
        val products = listOf(
            getProductUiModel(id = 1),
            getProductUiModel(id = 2),
            getProductUiModel(id = 3),
        )
        val paginatedProducts = PaginatedProducts(
            products = products,
            isLastPage = false,
            total = 100
        )
        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(any()) } returns paginatedProducts

        viewModel.initProductList()
        advanceUntilIdle()
        viewModel.onQueryChange(query)
        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content
            assertEquals(query, state.query)
            assertEquals(true, state.isLastPage)
        }
    }

    @Test
    fun `When query is empty Verify search query state is empty`() = runTest {
        val query = ""
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )
        val products = listOf(
            getProductUiModel(id = 1),
            getProductUiModel(id = 2),
            getProductUiModel(id = 3),
        )
        val paginatedProducts = PaginatedProducts(
            products = products,
            isLastPage = false,
            total = 100
        )
        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(any()) } returns paginatedProducts

        viewModel.initProductList()
        advanceUntilIdle()
        viewModel.onQueryChange(query)
        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content
            assertEquals(query, state.query)
            assertTrue(state.searchState is SearchState.Empty)
            assertEquals(false, state.isLastPage)
        }
    }

    @Test
    fun `When search verify search state is updated`() = runTest {
        val query = "Query"
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )
        val products = listOf(
            getProductUiModel(id = 1),
            getProductUiModel(id = 2),
            getProductUiModel(id = 3),
        )
        val searchedProducts = listOf(
            getProductUiModel(id = 1),
            getProductUiModel(id = 2),
            getProductUiModel(id = 3),
        )
        val paginatedProducts = PaginatedProducts(
            products = products,
            isLastPage = false,
            total = 100
        )
        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(any()) } returns paginatedProducts
        coEvery { repository.searchProducts(query) } returns searchedProducts

        viewModel.initProductList()
        advanceUntilIdle()
        viewModel.onQueryChange(query)
        viewModel.onSearch()
        advanceUntilIdle()
        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content
            assertEquals(query, state.query)
            assertEquals("", state.queryError)
            assertTrue(state.searchState is SearchState.Loaded)
            assertEquals(true, state.isLastPage)
            val searchState = state.searchState as SearchState.Loaded
            assertEquals(searchedProducts, searchState.searchedProducts)
        }
    }

    @Test
    fun `When search THEN no result verify search state is no result`() = runTest {
        val query = "Query"
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )
        val products = listOf(
            getProductUiModel(id = 1),
            getProductUiModel(id = 2),
            getProductUiModel(id = 3),
        )
        val paginatedProducts = PaginatedProducts(
            products = products,
            isLastPage = false,
            total = 100
        )
        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(any()) } returns paginatedProducts
        coEvery { repository.searchProducts(query) } returns emptyList()

        viewModel.initProductList()
        advanceUntilIdle()
        viewModel.onQueryChange(query)
        viewModel.onSearch()
        advanceUntilIdle()
        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content
            assertEquals(query, state.query)
            assertEquals("", state.queryError)
            assertTrue(state.searchState is SearchState.NoResult)
            assertEquals(true, state.isLastPage)
        }
    }

    @Test
    fun `When search Then if query length less than 3 verify state has queryError text`() =
        runTest {
            val query = "Qu"
            val categories = listOf(
                Category("Electronics", "electronics"),
                Category("Clothing", "clothing")
            )
            val products = listOf(
                getProductUiModel(id = 1),
                getProductUiModel(id = 2),
                getProductUiModel(id = 3),
            )
            val searchedProducts = listOf(
                getProductUiModel(id = 1),
                getProductUiModel(id = 2),
                getProductUiModel(id = 3),
            )
            val paginatedProducts = PaginatedProducts(
                products = products,
                isLastPage = false,
                total = 100
            )
            coEvery { repository.getCategories() } returns categories
            coEvery { repository.getProducts(any()) } returns paginatedProducts
            coEvery { repository.searchProducts(query) } returns searchedProducts

            viewModel.initProductList()
            advanceUntilIdle()
            viewModel.onQueryChange(query)
            viewModel.onSearch()
            advanceUntilIdle()
            viewModel.state.test {
                val state = awaitItem() as ProductListState.Content
                assertEquals(query, state.query)
                assertEquals("At least 3 characters.", state.queryError)
                assertTrue(state.searchState is SearchState.Empty)
                assertEquals(true, state.isLastPage)
            }
        }

    @Test
    fun `When sort selected verify filter state changed`() = runTest {
        val sortType = SortType.ALPHABETICALLY_A_Z
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )
        val products = listOf(
            getProductUiModel(id = 1),
            getProductUiModel(id = 2),
            getProductUiModel(id = 3),
        )
        val paginatedProducts = PaginatedProducts(
            products = products,
            isLastPage = false,
            total = 100
        )
        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(any()) } returns paginatedProducts

        viewModel.initProductList()
        advanceUntilIdle()
        viewModel.onSortSelect(sortType)
        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content
            assertEquals(sortType, state.filterState.selectedSort)
        }
    }

    @Test
    fun `When category selected verify filter state has changed`() = runTest {
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )
        val products = listOf(
            getProductUiModel(id = 1),
            getProductUiModel(id = 2),
            getProductUiModel(id = 3),
        )
        val paginatedProducts = PaginatedProducts(
            products = products,
            isLastPage = false,
            total = 100
        )
        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(any()) } returns paginatedProducts

        viewModel.initProductList()
        advanceUntilIdle()
        viewModel.onCategorySelect(categories.first())
        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content
            assertEquals(categories.first(), state.filterState.selectedCategory)
        }
    }

    @Test
    fun `When filters applied with category verify filter state and products`() = runTest {
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )
        val products = listOf(
            getProductUiModel(id = 1),
            getProductUiModel(id = 2),
            getProductUiModel(id = 3),
        )
        val paginatedProducts = PaginatedProducts(
            products = products,
            isLastPage = false,
            total = 100
        )
        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(1) } returns paginatedProducts
        coEvery {
            repository.getProducts(
                1,
                category = categories.first(),
                usePagination = any()
            )
        } returns paginatedProducts.copy(isLastPage = true)

        viewModel.initProductList()
        advanceUntilIdle()
        viewModel.onCategorySelect(categories.first())
        viewModel.onApplyFilters()
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content
            assertEquals(true, state.filterState.isApplied)
            assertEquals(false, state.filterState.isBottomSheetOpen)
            assertEquals(products, state.products)
        }
    }

    @Test
    fun `When filters applied with sort verify filter state and products`() = runTest {
        val sortType = SortType.PRICE_HIGH_TO_LOW
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )
        val products = listOf(
            getProductUiModel(id = 1, discountedPrice = 100),
            getProductUiModel(id = 2, discountedPrice = 300),
            getProductUiModel(id = 3, discountedPrice = 200),
        )
        val expectedProducts = products.sortedByDescending { it.discountedPrice }
        val paginatedProducts = PaginatedProducts(
            products = products,
            isLastPage = false,
            total = 100
        )
        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(1) } returns paginatedProducts
        coEvery {
            repository.getProducts(
                1,
                usePagination = false
            )
        } returns paginatedProducts.copy(isLastPage = true)

        viewModel.initProductList()
        advanceUntilIdle()
        viewModel.onSortSelect(sortType)
        viewModel.onApplyFilters()
        advanceUntilIdle()

        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content
            assertEquals(true, state.filterState.isApplied)
            assertEquals(false, state.filterState.isBottomSheetOpen)
            assertEquals(expectedProducts, state.products)
        }
    }

    @Test
    fun `When filters reset  verify default filter state`() = runTest {
        val categories = initViewModelWithDefault()
        viewModel.onCategorySelect(categories.first())
        viewModel.onResetFilters()

        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content
            assertEquals(defaultCategory(), state.filterState.selectedCategory)
            assertEquals(false, state.filterState.isBottomSheetOpen)
            assertEquals(SortType.DEFAULT, state.filterState.selectedSort)
        }
    }

    @Test
    fun `When filters reset if filter is applied verify get products called`() = runTest {
        val categories = initViewModelWithDefault()
        viewModel.onCategorySelect(categories.first())
        viewModel.onApplyFilters()
        advanceUntilIdle()
        viewModel.onResetFilters()
        advanceUntilIdle()

        coVerify(exactly = 3) { repository.getProducts(any(), any(), any()) }

        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content
            assertEquals(defaultCategory(), state.filterState.selectedCategory)
            assertEquals(false, state.filterState.isBottomSheetOpen)
            assertEquals(SortType.DEFAULT, state.filterState.selectedSort)
        }
    }

    @Test
    fun `When open filters verify bottom sheet is open`() = runTest {
        initViewModelWithDefault()
        viewModel.onOpenFilters()

        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content
            assertEquals(true, state.filterState.isBottomSheetOpen)
        }
    }

    @Test
    fun `When dismiss filters verify bottom sheet is closed`() = runTest {
        initViewModelWithDefault()
        viewModel.onDismissFilters()

        viewModel.state.test {
            val state = awaitItem() as ProductListState.Content
            assertEquals(false, state.filterState.isBottomSheetOpen)
        }
    }

    private fun TestScope.initViewModelWithDefault(): List<Category> {
        val categories = listOf(
            Category("Electronics", "electronics"),
            Category("Clothing", "clothing")
        )
        val products = listOf(
            getProductUiModel(id = 1, discountedPrice = 100),
            getProductUiModel(id = 2, discountedPrice = 300),
            getProductUiModel(id = 3, discountedPrice = 200),
        )
        val paginatedProducts = PaginatedProducts(
            products = products,
            isLastPage = false,
            total = 100
        )
        coEvery { repository.getCategories() } returns categories
        coEvery { repository.getProducts(1) } returns paginatedProducts

        viewModel.initProductList()
        advanceUntilIdle()
        return categories
    }
}