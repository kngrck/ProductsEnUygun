package com.example.productsenuygun.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ErrorView(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(message, modifier = Modifier.align(Alignment.Center))
    }
}