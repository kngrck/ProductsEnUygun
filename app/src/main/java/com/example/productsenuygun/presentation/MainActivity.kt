package com.example.productsenuygun.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import com.example.productsenuygun.presentation.common.AppBar
import com.example.productsenuygun.presentation.navigation.BottomNavBar
import com.example.productsenuygun.presentation.navigation.NavigationHost
import com.example.productsenuygun.presentation.ui.theme.ProductsEnUygunTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductsEnUygunTheme {
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        AppBar(navController)
                    },
                    bottomBar = {
                        BottomNavBar(navController)
                    }
                ) { padding ->
                    NavigationHost(navController, padding)
                }
            }
        }
    }
}
