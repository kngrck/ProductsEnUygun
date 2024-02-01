package com.example.productsenuygun.presentation.productlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.presentation.common.ErrorView
import com.example.productsenuygun.presentation.common.LoadingIndicator

@Composable
fun ProductListingView(
    navController: NavController,
    viewModel: ProductListViewModel = hiltViewModel()
) {

    when (val state = viewModel.state.collectAsState().value) {
        is ProductListState.Loading -> LoadingIndicator()
        is ProductListState.Error -> ErrorView(state.message)
        is ProductListState.Content -> ProductListContent(state)
    }
}

@Composable
fun ProductListContent(content: ProductListState.Content) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(content.products) { product ->
            ProductListItem(product = product)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProductListItem(product: ProductUiModel) {
    Card {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = product.images.first()),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.background),
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column() {
                    Text(
                        text = product.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = product.description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.width(32.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "",
                        tint = Color.Yellow,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${product.rating}", style = MaterialTheme.typography.labelSmall)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "${product.price} TL", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

    }
}

@Preview
@Composable
fun ProductItemPreview() {
    ProductListItem(
        ProductUiModel(
            brand = "Dummy Brand",
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
            id = 0
        )
    )
}

