package com.example.productsenuygun.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartProduct(
    @PrimaryKey
    val id: Int,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("quantity") val quantity: Int,
    @ColumnInfo("price") val price: Int,
    @ColumnInfo("discountPrice") val discountedPrice: Int
)
