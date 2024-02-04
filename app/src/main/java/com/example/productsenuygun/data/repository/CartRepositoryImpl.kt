package com.example.productsenuygun.data.repository

import com.example.productsenuygun.data.local.AppDatabase
import com.example.productsenuygun.domain.mapper.toCartProduct
import com.example.productsenuygun.domain.mapper.toUiModel
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CartRepositoryImpl(
    private val localDatabase: AppDatabase
) : CartRepository {

    private val cartDao = localDatabase.cartDao()
    private val _cartTotalItems = MutableSharedFlow<Int>()

    override val cartTotalItems: SharedFlow<Int>
        get() = _cartTotalItems.asSharedFlow()

    override suspend fun getCartProducts(): List<ProductUiModel> {
        return cartDao.getAll().map { it.toUiModel() }
    }

    override suspend fun getCartProductById(id: Int): ProductUiModel? {
        return cartDao.getCartProductById(id)?.toUiModel()
    }

    override suspend fun increaseQuantityById(productUiModel: ProductUiModel) {
        val cartProduct = productUiModel.toCartProduct()
        val product = getCartProductById(cartProduct.id)
        if (product == null) cartDao.addProduct(cartProduct)

        cartDao.increaseQuantityById(cartProduct.id)
        _cartTotalItems.emit(getTotalItemsInCart())
    }

    override suspend fun decreaseQuantityById(id: Int) {
        val product = getCartProductById(id)

        if (product?.quantity == 1) {
            removeProductById(product.id)
            return
        }

        cartDao.decreaseQuantityById(id)
        _cartTotalItems.emit(getTotalItemsInCart())
    }

    override suspend fun removeProductById(id: Int) {
        cartDao.deleteProductById(id)
        _cartTotalItems.emit(getTotalItemsInCart())
    }

    override suspend fun emptyCart() {
        cartDao.emptyCart()
        _cartTotalItems.emit(getTotalItemsInCart())
    }

    override suspend fun initCart() {
        _cartTotalItems.emit(getTotalItemsInCart())
    }

    private suspend fun getTotalItemsInCart(): Int {
        val cartProducts = cartDao.getAll()
        if (cartProducts.isEmpty()) return 0
        return cartProducts.sumOf { it.quantity }
    }
}