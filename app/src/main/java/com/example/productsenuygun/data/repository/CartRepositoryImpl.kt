package com.example.productsenuygun.data.repository

import com.example.productsenuygun.data.local.AppDatabase
import com.example.productsenuygun.domain.mapper.toCartProduct
import com.example.productsenuygun.domain.mapper.toUiModel
import com.example.productsenuygun.domain.model.CartProductUiModel
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.domain.repository.CartRepository

class CartRepositoryImpl(
    private val localDatabase: AppDatabase
) : CartRepository {
    private val cartDao = localDatabase.cartDao()

    override suspend fun getCartProducts(): List<CartProductUiModel> {
        return cartDao.getAll().map { it.toUiModel() }
    }

    override suspend fun getCartProductById(id: Int): CartProductUiModel? {
        return cartDao.getCartProductById(id)?.toUiModel()
    }

    override suspend fun increaseQuantityById(productUiModel: ProductUiModel) {
        val cartProduct = productUiModel.toCartProduct()
        val product = getCartProductById(cartProduct.id)
        if (product == null) cartDao.addProduct(cartProduct)
        return cartDao.increaseQuantityById(cartProduct.id)
    }

    override suspend fun decreaseQuantityById(id: Int) {
        val product = getCartProductById(id)

        if (product?.quantity == 1) {
            cartDao.deleteProductById(id)
            return
        }

        return cartDao.decreaseQuantityById(id)
    }

}