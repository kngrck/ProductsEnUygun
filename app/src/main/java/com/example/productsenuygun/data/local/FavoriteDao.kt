package com.example.productsenuygun.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.productsenuygun.data.local.entity.FavoriteProduct

@Dao
interface FavoriteDao {

    @Query("SELECT * from favorite")
    suspend fun getAllFavorites(): List<FavoriteProduct>

    @Query("SELECT * from favorite WHERE id = :id")
    suspend fun getFavoriteById(id: Int): FavoriteProduct?

    @Insert
    suspend fun addFavorite(product: FavoriteProduct)

    @Query("DELETE FROM favorite WHERE id = :id")
    suspend fun deleteFavoriteById(id: Int)
}