package com.example.productsenuygun.presentation.navigation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomNavBar(
    navController: NavController
) {
    val navigationItems = listOf(
        NavigationItem.ProductList,
        NavigationItem.Favorites,
        NavigationItem.Cart,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    BottomAppBar(
        modifier = Modifier.clip(RoundedCornerShape(topStartPercent = 40, topEndPercent = 40))
    ) {
        navigationItems.forEach { navItem ->
            NavigationBarItem(
                selected = navBackStackEntry?.destination?.route == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.startDestinationRoute!!) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.route
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    BottomNavBar(navController = rememberNavController())
}