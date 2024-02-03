package com.example.productsenuygun.presentation.productdetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.productsenuygun.domain.model.ProductUiModel
import com.example.productsenuygun.presentation.common.ErrorView
import com.example.productsenuygun.presentation.common.LoadingIndicator

@Composable
fun ProductDetailView(
    navController: NavController,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {

    when (val state = viewModel.viewState.collectAsState().value) {
        is ProductDetailState.Loading -> LoadingIndicator()
        is ProductDetailState.Error -> ErrorView(message = state.message)
        is ProductDetailState.Content -> ProductDetailContent(
            navController,
            state,
            onFavoriteClick = viewModel::onFavoriteClick
        )
    }
}

@Composable
fun ProductDetailContent(
    navController: NavController,
    content: ProductDetailState.Content,
    onFavoriteClick: () -> Unit
) {
    val product = content.product
    val isDiscounted = product.discountPrice > 0

    Column(modifier = Modifier.fillMaxSize()) {
        ProductDetailTopBar(
            navController = navController,
            product = product,
            onFavoriteClick = onFavoriteClick
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(300.dp)
        ) {
            ImageSlider(product.images)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f, fill = false),
                maxLines = 2
            )
            PriceSection(isDiscounted, product)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailTopBar(
    navController: NavController,
    product: ProductUiModel,
    onFavoriteClick: () -> Unit
) {
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
                text = product.title,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1
            )
        },
        actions = { FavoriteButton(isFavorite = product.isFavorite, onClick = onFavoriteClick) }
    )
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            tint = if (isFavorite) Color.Red else Color.Black,
            contentDescription = "Favorite Button"
        )
    }
}

@Composable
private fun PriceSection(
    isDiscounted: Boolean,
    product: ProductUiModel
) {
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
                text = "${product.discountPrice} $",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxScope.ImageSlider(images: List<String>) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        images.size
    }

    HorizontalPager(state = pagerState) {
        Image(
            painter = rememberAsyncImagePainter(model = images[it]),
            contentDescription = "Product Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillWidth,
        )
    }

    Row(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 8.dp)
    ) {
        images.forEachIndexed { i, _ ->
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .then(
                        if (pagerState.currentPage == i) {
                            Modifier.background(
                                Color.Black
                            )
                        } else {
                            Modifier.background(
                                Color.Gray
                            )
                        }
                    )
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
