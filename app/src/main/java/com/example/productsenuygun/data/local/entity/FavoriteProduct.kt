package com.example.productsenuygun.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class FavoriteProduct(
    @PrimaryKey
    val id: Int,
    @ColumnInfo("name") val name: String,
    @ColumnInfo("price") val price: Int,
    @ColumnInfo("discountPrice") val discountedPrice: Int,
    @ColumnInfo("image") val image: String,
)