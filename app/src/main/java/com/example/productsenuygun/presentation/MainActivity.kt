package com.example.productsenuygun.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productsenuygun.presentation.navigation.NavigationItem
import com.example.productsenuygun.presentation.productlist.ProductListingView
import com.example.productsenuygun.presentation.ui.theme.ProductsEnUygunTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductsEnUygunTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = NavigationItem.ProductList.route
                ) {
                    composable(NavigationItem.ProductList.route) { ProductListingView(navController) }
                }
            }
        }
    }
}
