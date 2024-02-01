package com.example.productsenuygun.presentation.navigation

enum class Screen {
    PRODUCT_LIST,
}

sealed class NavigationItem(val route: String) {
    data object ProductList : NavigationItem(Screen.PRODUCT_LIST.name)
}