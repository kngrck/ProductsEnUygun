package com.example.productsenuygun.presentation.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.presentation.common.AddToCart
import com.example.productsenuygun.presentation.common.ErrorView
import com.example.productsenuygun.presentation.common.LoadingIndicator
import com.example.productsenuygun.presentation.common.SummaryItem
import com.example.productsenuygun.presentation.navigation.NavigationItem

@Composable
fun CartView(navController: NavController, viewModel: CartViewModel = hiltViewModel()) {

    LaunchedEffect(Unit) {
        viewModel.initCart()
    }

    when (val state = viewModel.viewState.collectAsState().value) {
        is CartState.Content -> CartViewContent(
            navController = navController,
            content = state,
            onDecreaseQuantity = viewModel::onDecreaseQuantity,
            onIncreaseQuantity = viewModel::onIncreaseQuantity,
            onRemoveFromCart = viewModel::onRemoveFromCart,
        )

        is CartState.Error -> ErrorView(message = state.message)
        CartState.Loading -> LoadingIndicator()
    }
}

@Composable
private fun CartViewContent(
    navController: NavController,
    content: CartState.Content,
    onDecreaseQuantity: (ProductUiModel) -> Unit,
    onIncreaseQuantity: (ProductUiModel) -> Unit,
    onRemoveFromCart: (ProductUiModel) -> Unit
) {
    if (content.products.isEmpty()) {
        ErrorView(message = "Your cart is empty!")
    } else {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                items(content.products) { product ->
                    CartItem(
                        product = product,
                        onDecreaseQuantity = { onDecreaseQuantity(product) },
                        onIncreaseQuantity = { onIncreaseQuantity(product) },
                        onRemoveFromCart = { onRemoveFromCart(product) })
                }
            }
            Column {
                Summary(content)
                Button(
                    onClick = { navController.navigate(NavigationItem.Checkout.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Checkout")
                }
            }
        }
    }
}

@Composable
private fun Summary(
    content: CartState.Content
) {
    Column(modifier = Modifier.padding(16.dp)) {
        SummaryItem(title = "Total price:", value = "${content.totalPrice} $")
        SummaryItem(title = "Total discount:", value = "${content.totalDiscount} $")
        SummaryItem(title = "Total:", value = "${content.total} $")
    }
}

@Composable
private fun CartItem(
    product: ProductUiModel,
    onDecreaseQuantity: () -> Unit,
    onIncreaseQuantity: () -> Unit,
    onRemoveFromCart: () -> Unit,
) {
    val isDiscounted = product.discountedPrice > 0
    val quantity = product.quantity

    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, false)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = product.images.firstOrNull().orEmpty()
                        ),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.background),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = product.title,
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${product.price} $",
                                textDecoration = if (isDiscounted) TextDecoration.LineThrough else null,
                                style = if (isDiscounted) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelLarge,
                                color = if (isDiscounted) Color.Gray else Color.Black
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            if (isDiscounted) {
                                Text(
                                    text = "${product.discountedPrice} $",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }

                AddToCart(
                    quantity = quantity,
                    onIncreaseQuantity = onIncreaseQuantity,
                    onDecreaseQuantity = onDecreaseQuantity,
                )
            }
        }

        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = "Remove from cart",
            tint = Color.Red,
            modifier = Modifier
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .background(Color.White)
                .padding(4.dp)
                .clickable { onRemoveFromCart() }
        )
    }
}
