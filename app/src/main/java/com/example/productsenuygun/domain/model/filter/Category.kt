package com.example.productsenuygun.domain.model.filter

data class Category(
    val name: String,
    val value: String,
)

fun defaultCategory() = Category("All", "all")