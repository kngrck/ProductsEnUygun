package com.example.productsenuygun.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.productsenuygun.data.local.entity.CartProduct

@Dao
interface CartDao {

    @Query("SELECT * from cart")
    suspend fun getAll(): List<CartProduct>

    @Query("SELECT * from cart WHERE id = :id")
    suspend fun getCartProductById(id: Int): CartProduct?

    @Insert
    suspend fun addProduct(product: CartProduct)

    @Query("UPDATE cart SET quantity = quantity + 1 WHERE id = :id")
    suspend fun increaseQuantityById(id: Int)

    @Query("UPDATE cart SET quantity = quantity - 1 WHERE id = :id AND quantity > 0")
    suspend fun decreaseQuantityById(id: Int)

    @Query("DELETE FROM cart WHERE id = :id")
    suspend fun deleteProductById(id: Int)

    @Query("DELETE FROM cart")
    suspend fun emptyCart()
}