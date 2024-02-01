package com.example.productsenuygun.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.productsenuygun.presentation.cart.CartView
import com.example.productsenuygun.presentation.favorites.FavoritesView
import com.example.productsenuygun.presentation.productlist.ProductListingView

@Composable
fun NavigationHost(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.ProductList.route,
        modifier = Modifier.padding(
            bottom = padding.calculateBottomPadding(),
            top = padding.calculateTopPadding()
        )
    ) {
        composable(NavigationItem.ProductList.route) {
            ProductListingView(navController)
        }
        composable(NavigationItem.Favorites.route) {
            FavoritesView(navController)
        }
        composable(NavigationItem.Cart.route) {
            CartView(navController)
        }
    }
}