package com.example.productsenuygun.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.productsenuygun.presentation.cart.CartView
import com.example.productsenuygun.presentation.checkout.CheckoutView
import com.example.productsenuygun.presentation.favorites.FavoritesView
import com.example.productsenuygun.presentation.productdetail.ProductDetailView
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
        composable(
            NavigationItem.ProductList.route,
            enterTransition = null,
            exitTransition = null,
            popEnterTransition = null,
            popExitTransition = null

        ) {
            ProductListingView(navController)
        }
        composable(
            NavigationItem.Favorites.route,
            enterTransition = null,
            exitTransition = null,
            popEnterTransition = null,
            popExitTransition = null
        ) {
            FavoritesView(navController)
        }
        composable(
            NavigationItem.Cart.route,
            enterTransition = null,
            exitTransition = null,
            popEnterTransition = null,
            popExitTransition = null
        ) {
            CartView(navController)
        }
        composable(
            NavigationItem.Checkout.route,
            enterTransition = null,
            exitTransition = null,
            popEnterTransition = null,
            popExitTransition = null
        ) {
            CheckoutView(navController)
        }
        composable(
            "${NavigationItem.ProductDetail.route}/{${Arguments.PRODUCT_ID.name}}",
            arguments = listOf(navArgument(Arguments.PRODUCT_ID.name) {
                type = NavType.IntType
            }),
            enterTransition = null,
            exitTransition = null,
            popEnterTransition = null,
            popExitTransition = null
        ) {
            ProductDetailView(navController = navController)
        }
    }
}