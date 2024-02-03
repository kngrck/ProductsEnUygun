package com.example.productsenuygun.presentation.productlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.productsenuygun.domain.model.filter.Category
import com.example.productsenuygun.domain.model.filter.SortType
import com.example.productsenuygun.domain.model.filter.defaultCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    filterState: FilterState,
    onSortSelect: (sortType: SortType) -> Unit,
    onCategorySelect: (category: Category) -> Unit,
    onApplyFilters: () -> Unit,
    onResetFilters: () -> Unit,
    onDismissFilters: () -> Unit,
) {
    val defaultCategory = defaultCategory()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(sheetState.isVisible) {
        if (!sheetState.isVisible) {
            onDismissFilters()
        }
    }
    if (filterState.isBottomSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {},
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1f, false)
                ) {
                    Text(text = "Sort")
                    Spacer(modifier = Modifier.height(8.dp))
                    SortType.entries.forEach { sortType ->
                        FilterItem(
                            name = sortType.value,
                            isSelected = filterState.selectedSort == sortType,
                            onSelect = { onSortSelect(sortType) })

                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (filterState.categories.isNotEmpty()) {
                        Text(text = "Filters")
                        Spacer(modifier = Modifier.height(8.dp))
                        FilterItem(
                            name = defaultCategory.name,
                            isSelected = filterState.selectedCategory == defaultCategory,
                            onSelect = { onCategorySelect(defaultCategory) }
                        )
                        filterState.categories.forEach { category ->
                            FilterItem(
                                category.name,
                                filterState.selectedCategory == category,
                                onSelect = { onCategorySelect(category) })
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(60.dp)
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            disabledContainerColor = Color.Gray
                        ),
                        onClick = onResetFilters,
                        enabled = filterState.selectedSort != SortType.DEFAULT || filterState.selectedCategory != defaultCategory
                    ) {
                        Text(text = "Reset")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onApplyFilters,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                    ) {
                        Text(text = "Apply")
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterItem(
    name: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect
        )
        Text(text = name)
    }
}
