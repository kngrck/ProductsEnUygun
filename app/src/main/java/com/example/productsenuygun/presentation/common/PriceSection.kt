package com.example.productsenuygun.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun PriceSection(discountedPrice: Int, price: Int, modifier: Modifier = Modifier) {
    val isDiscounted = discountedPrice > 0
    Column(
        modifier = modifier.padding(bottom = 8.dp, end = 8.dp)
    ) {
        if (isDiscounted) {
            Text(
                text = "$discountedPrice $",
                style = MaterialTheme.typography.labelLarge,
            )
        }
        Text(
            text = "$price $",
            textDecoration = if (isDiscounted) TextDecoration.LineThrough else null,
            style = if (isDiscounted) MaterialTheme.typography.labelSmall else MaterialTheme.typography.labelLarge,
            color = if (isDiscounted) Color.Gray else Color.Black
        )
    }
}