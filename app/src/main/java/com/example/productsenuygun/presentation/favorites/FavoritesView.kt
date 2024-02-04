package com.example.productsenuygun.presentation.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.presentation.common.AddToCart
import com.example.productsenuygun.presentation.common.ErrorView
import com.example.productsenuygun.presentation.common.FavoriteButton
import com.example.productsenuygun.presentation.common.LoadingIndicator

@Composable
fun FavoritesView(navController: NavController, viewModel: FavoritesViewModel = hiltViewModel()) {
    val state = viewModel.viewState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.initFavorites()
    }

    when (state) {
        is FavoritesState.Content -> FavoritesContent(
            state,
            onFavoriteClick = viewModel::removeFavorite,
            onIncreaseQuantity = viewModel::onIncreaseQuantity,
            onDecreaseQuantity = viewModel::onDecreaseQuantity,
        )

        is FavoritesState.Error -> ErrorView(message = state.message)
        FavoritesState.Loading -> LoadingIndicator()
    }
}

@Composable
private fun FavoritesContent(
    content: FavoritesState.Content,
    onFavoriteClick: (id: Int) -> Unit,
    onIncreaseQuantity: (product: ProductUiModel) -> Unit,
    onDecreaseQuantity: (id: Int) -> Unit
) {
    if (content.products.isEmpty()) {
        ErrorView(message = "Your favorites is empty")
    } else {
        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(8.dp)) {
            items(content.products) { product ->
                FavoriteItem(
                    product = product,
                    onFavoriteClick = { onFavoriteClick(product.id) },
                    onIncreaseQuantity = onIncreaseQuantity,
                    onDecreaseQuantity = onDecreaseQuantity
                )
            }
        }
    }
}

@Composable
private fun FavoriteItem(
    product: ProductUiModel,
    onFavoriteClick: () -> Unit,
    onIncreaseQuantity: (product: ProductUiModel) -> Unit,
    onDecreaseQuantity: (id: Int) -> Unit
) {
    Card(
        modifier = Modifier
            .width(136.dp)
            .padding(8.dp)
    ) {
        Column(
        ) {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(model = product.images.first()),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(MaterialTheme.colorScheme.background),
                    contentScale = ContentScale.Crop,
                )
                FavoriteButton(
                    isFavorite = true,
                    onClick = onFavoriteClick,
                    modifier = Modifier.align(
                        Alignment.TopEnd
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.title,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${product.discountedPrice} $",
                modifier = Modifier.padding(horizontal = 8.dp),
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            AddToCart(
                quantity = product.quantity,
                onIncreaseQuantity = { onIncreaseQuantity(product) },
                onDecreaseQuantity = { onDecreaseQuantity(product.id) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
