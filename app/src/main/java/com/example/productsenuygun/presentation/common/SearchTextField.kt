package com.example.productsenuygun.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    text: String,
    onValueChange: (text: String) -> Unit,
    onSearch: () -> Unit,
    errorText: String = ""
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        TextField(
            value = text,
            onValueChange = { onValueChange(it) },
            maxLines = 1,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search field"
                )
            },
            placeholder = { Text(text = "Search", style = MaterialTheme.typography.bodySmall) },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch()
                if (errorText.isEmpty()) keyboardController?.hide()
            }),
            isError = errorText.isNotEmpty()
        )

        if (errorText.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorText,
                color = Color.Red,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}