package com.example.productsenuygun.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AddToCart(
    quantity: Int,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(CircleShape)
            .background(Color.White)
    ) {
        if (quantity > 0) {
            Icon(
                imageVector = if (quantity == 1) {
                    Icons.Outlined.Delete
                } else {
                    Icons.Default.Remove
                },
                contentDescription = "Remove from cart",
                tint = if (quantity == 1) Color.Red else Color.Black,
                modifier = Modifier
                    .padding(4.dp)
                    .clip(CircleShape)
                    .clickable { onDecreaseQuantity() }
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = quantity.toString(),
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add to cart",
            modifier = Modifier
                .padding(4.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onIncreaseQuantity() }
        )
    }
}