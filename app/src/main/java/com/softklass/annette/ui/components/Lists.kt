package com.softklass.annette.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import com.softklass.annette.data.database.dao.BalanceSheetItemWithValue

@Composable
fun BalanceSheetItemList(
    items: List<BalanceSheetItemWithValue>,
    onNavigateToDetail: (Int, String, String, String) -> Unit
) {
    val grouped = items.groupBy { it.category }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        grouped.forEach { (category, groupItems) ->
            item {
                Text(
                    text = category,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(groupItems) { item ->
                BalanceSheetItemCard(
                    item = item,
                    onClick = {
                        onNavigateToDetail(
                            item.id.toInt(),
                            item.name,
                            item.category,
                            item.type
                        )
                    }
                )
            }
        }
    }
}