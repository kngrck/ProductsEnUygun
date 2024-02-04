package com.example.productsenuygun.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productsenuygun.data.local.entity.CartProduct
import com.example.productsenuygun.data.local.entity.FavoriteProduct

@Database(entities = [CartProduct::class, FavoriteProduct::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    abstract fun favoriteDao(): FavoriteDao
}