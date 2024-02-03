package com.example.productsenuygun.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productsenuygun.data.local.entity.CartProduct

@Database(entities = [CartProduct::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
}