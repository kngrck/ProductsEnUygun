package com.example.productsenuygun.presentation.checkout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.presentation.common.ErrorView
import com.example.productsenuygun.presentation.common.LoadingIndicator
import com.example.productsenuygun.presentation.common.SummaryItem

@Composable
fun CheckoutView(
    navController: NavController,
    viewModel: CheckoutViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.initCheckout()
    }

    when (val state = viewModel.viewState.collectAsState().value) {
        is CheckoutState.Loading -> LoadingIndicator()
        is CheckoutState.Error -> ErrorView(message = state.message)
        is CheckoutState.Content -> CheckoutContent(
            navController = navController,
            content = state,
            onProceed = viewModel::onProceed,
        )
    }
}

@Composable
fun CheckoutContent(
    navController: NavController,
    content: CheckoutState.Content,
    onProceed: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
    ) {
        CheckoutTopBar(navController = navController)
        LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
            items(content.products) { product ->
                CheckoutItem(product = product)
                Divider()
            }
        }
        Column {
            Summary(content)
            Button(
                onClick = onProceed,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Order")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CheckoutTopBar(navController: NavController) {
    TopAppBar(
        navigationIcon = {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { navController.popBackStack() })
        },
        title = {
            Text(
                text = "Order Summary",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1
            )
        }
    )
}

@Composable
private fun Summary(
    content: CheckoutState.Content
) {
    Column(modifier = Modifier.padding(16.dp)) {
        SummaryItem(title = "Total price:", value = "${content.totalPrice} $")
        SummaryItem(title = "Total discount:", value = "${content.totalDiscount} $")
        SummaryItem(title = "Total:", value = "${content.total} $")
    }
}

@Composable
private fun CheckoutItem(
    product: ProductUiModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = product.title,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 2,
            modifier = Modifier.weight(1f, fill = false)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(100.dp)
        ) {
            Text(
                text = "${product.quantity}x",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${product.discountedPrice * product.quantity}$",
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Preview
@Composable
private fun CheckoutItemPreview() {
    CheckoutContent(
        navController = rememberNavController(),
        onProceed = {},
        content = CheckoutState.Content(
            products = listOf(
                ProductUiModel(
                    category = "Dummy Category Dummy Category Dummy Category Dummy Category Dummy Category Dummy Category Dummy Category Dummy Category Dummy Category Dummy Category",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    discountPercentage = 10.0,
                    images = listOf(
                        "https://example.com/image1.jpg",
                        "https://example.com/image2.jpg",
                        "https://example.com/image3.jpg"
                    ),
                    price = 100,
                    rating = 4.5,
                    stock = 50,
                    thumbnail = "https://example.com/thumbnail.jpg",
                    title = "Dummy Product Dummy ProductDummy ProductDummy ProductDummy ProductDummy ProductDummy ProductDummy ProductDummy Product",
                    id = 0,
                    discountedPrice = 90
                ),
                ProductUiModel(
                    category = "Dummy Category",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    discountPercentage = 10.0,
                    images = listOf(
                        "https://example.com/image1.jpg",
                        "https://example.com/image2.jpg",
                        "https://example.com/image3.jpg"
                    ),
                    price = 100,
                    rating = 4.5,
                    stock = 50,
                    thumbnail = "https://example.com/thumbnail.jpg",
                    title = "Dummy Product",
                    id = 0,
                    discountedPrice = 90
                ),
                ProductUiModel(
                    category = "Dummy Category",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    discountPercentage = 10.0,
                    images = listOf(
                        "https://example.com/image1.jpg",
                        "https://example.com/image2.jpg",
                        "https://example.com/image3.jpg"
                    ),
                    price = 100,
                    rating = 4.5,
                    stock = 50,
                    thumbnail = "https://example.com/thumbnail.jpg",
                    title = "Dummy Product",
                    id = 0,
                    discountedPrice = 90
                )
            ),
            total = 40,
            totalPrice = 40,
            totalDiscount = 40,
        )
    )
}