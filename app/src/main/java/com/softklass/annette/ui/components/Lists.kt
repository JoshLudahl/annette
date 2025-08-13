package com.softklass.annette.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BalanceSheetItemList(
    items: List<BalanceSheetItemWithValue>,
    onNavigateToDetail: (Int, String, String, String) -> Unit,
    onLonPress: (BalanceSheetItemWithValue) -> Unit
) {
    val grouped = items.groupBy { it.category }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        grouped.forEach { (category, groupItems) ->
            item {
                Row {
                    Text(
                        text = category,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Spacer(Modifier.weight(1f))

                    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
                    val groupTotal = groupItems.sumOf { it.value ?: 0.0 }
                    Text(
                        text = "${currencyFormat.format(groupTotal)}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

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
                    },
                    onLongPress = {
                        onLonPress(item)
                    }
                )
            }
        }
    }
}