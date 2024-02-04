package com.example.productsenuygun.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.productsenuygun.domain.model.ProductUiModel

@Composable
fun ProductListItem(
    product: ProductUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .height(96.dp)
                .padding(horizontal = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterStart)
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
                    Column {
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
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 8.dp, end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "",
                            tint = Color.Yellow,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${product.rating}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    PriceSection(product)
                }
            }
        }

    }
}

@Composable
private fun PriceSection(product: ProductUiModel) {
    val isDiscounted = product.discountedPrice > 0
    Column(
        modifier = Modifier.padding(bottom = 8.dp, end = 8.dp)
    ) {
        if (isDiscounted) {
            Text(
                text = "${product.discountedPrice} $",
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Text(
            text = "${product.price} $",
            textDecoration = if (isDiscounted) TextDecoration.LineThrough else null,
            style = if (isDiscounted) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelLarge,
            color = if (isDiscounted) Color.Gray else Color.Black
        )
    }
}

@Preview
@Composable
fun ProductItemPreview() {
    ProductListItem(
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
        onClick = {}
    )
}