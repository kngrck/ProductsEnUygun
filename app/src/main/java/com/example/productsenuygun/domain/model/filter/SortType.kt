package com.example.productsenuygun.domain.model.filter

enum class SortType(val value: String) {
    DEFAULT("Default"),
    ALPHABETICALLY_A_Z("Alphabetically A-Z"),
    ALPHABETICALLY_Z_A("Alphabetically Z-A"),
    PRICE_LOW_TO_HIGH("Price low to high"),
    PRICE_HIGH_TO_LOW("Price high to low")
}