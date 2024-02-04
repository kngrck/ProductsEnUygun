package com.example.productsenuygun.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

enum class Screen {
    PRODUCT_LIST,
    FAVORITES,
    CART,
    PRODUCT_DETAIL,
    CHECKOUT
}

enum class Arguments {
    PRODUCT_ID
}

sealed class NavigationItem(val route: String, val title: String, val icon: ImageVector) {
    data object ProductList : NavigationItem(
        route = Screen.PRODUCT_LIST.name,
        title = "Products",
        icon = Icons.Default.Home
    )

    data object ProductDetail : NavigationItem(
        route = Screen.PRODUCT_DETAIL.name,
        title = "Product Detail",
        icon = Icons.Default.Home
    )

    data object Favorites : NavigationItem(
        route = Screen.FAVORITES.name,
        title = "Favorites",
        icon = Icons.Default.Favorite
    )

    data object Cart : NavigationItem(
        route = Screen.CART.name,
        title = "Cart",
        icon = Icons.Default.ShoppingCart
    )

    data object Checkout : NavigationItem(
        route = Screen.CHECKOUT.name,
        title = "Checkout",
        icon = Icons.Default.ShoppingCart
    )
}

@Composable
fun currentNavigationItem(navController: NavController): NavigationItem? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val route = navBackStackEntry?.destination?.route

    return when {
        route == NavigationItem.ProductList.route -> NavigationItem.ProductList
        route == NavigationItem.Favorites.route -> NavigationItem.Favorites
        route == NavigationItem.Cart.route -> NavigationItem.Cart
        route == NavigationItem.Checkout.route -> NavigationItem.Checkout
        route != null && route.contains(NavigationItem.ProductDetail.route) -> NavigationItem.ProductDetail
        else -> null
    }
}