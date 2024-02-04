package com.example.productsenuygun.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomNavBar(
    navController: NavController,
    viewModel: BottomNavigationViewModel = hiltViewModel(),
) {
    val totalCartItems = viewModel.cartItems.collectAsState(0).value
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
                    if (navItem is NavigationItem.Cart) {
                        Box(modifier = Modifier.size(48.dp)) {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = navItem.route,
                                modifier = Modifier.align(Alignment.Center)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .clip(CircleShape)
                                    .size(20.dp)
                                    .background(Color.Red.copy(alpha = 0.7f))
                            ) {
                                Text(
                                    text = totalCartItems.toString(),
                                    color = Color.White,
                                    style = TextStyle(fontSize = 12.sp),
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }

                    } else {
                        Icon(
                            imageVector = navItem.icon,
                            contentDescription = navItem.route
                        )
                    }
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