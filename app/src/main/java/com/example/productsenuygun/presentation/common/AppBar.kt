package com.example.productsenuygun.presentation.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.productsenuygun.presentation.navigation.currentNavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavController) {
    val currentNavigationItem = currentNavigationItem(navController)

    TopAppBar(
        title = { Text(text = currentNavigationItem?.title.orEmpty()) },
    )
}