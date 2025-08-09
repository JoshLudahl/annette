package com.softklass.annette.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.Flight
import androidx.compose.material.icons.rounded.FoodBank
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocalHospital
import androidx.compose.material.icons.rounded.Money
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softklass.annette.currencyFormatter
import com.softklass.annette.data.database.dao.BalanceSheetItemWithValue
import com.softklass.annette.data.model.BalanceSheetType
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BalanceSheetHeaderCard(
    items: List<BalanceSheetItemWithValue>,
    type: BalanceSheetType,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (type) {
                BalanceSheetType.ASSETS -> MaterialTheme.colorScheme.primary
                BalanceSheetType.LIABILITIES -> MaterialTheme.colorScheme.error
            }
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val label = when (type) {
                BalanceSheetType.ASSETS -> "Assets"
                BalanceSheetType.LIABILITIES -> "Liabilities"
            }

            Text(
                text = "Total $label",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currencyFormatter.format(items.sumOf { it.value ?: 0.0 }),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun EmptyBalanceSheetListCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "The list is empty.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap the + button to add items",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BalanceSheetItemCard(
    item: BalanceSheetItemWithValue,
    onClick: () -> Unit = {},
    onLongPress: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongPress
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = resolveIconForCategory(item.category),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.category,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = currencyFormat.format(item.value ?: 0.0),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

private fun resolveIconForCategory(category: String): ImageVector {
    return when (category.lowercase()) {
        "food", "restaurant", "dining" -> Icons.Rounded.Restaurant
        "transport", "transportation" -> Icons.Rounded.DirectionsBus
        "entertainment", "movies", "cinema", "streaming" -> Icons.Rounded.Movie
        "shopping", "retail", "clothing" -> Icons.Rounded.ShoppingCart
        "utilities", "internet", "electricity", "water", "real estate", "house", "home" -> Icons.Rounded.Home
        "health", "medical", "pharmacy", "doctor" -> Icons.Rounded.LocalHospital
        "education", "books", "school", "university" -> Icons.Rounded.School
        "travel", "vacation", "hotel", "airplane", "tourism" -> Icons.Rounded.Flight
        "groceries", "supermarket", "market", "farmers market" -> Icons.Rounded.FoodBank
        "credit card" -> Icons.Rounded.CreditCard
        "loan" -> Icons.Rounded.AttachMoney
        "cash" -> Icons.Rounded.Money
        "investments", "retirement" -> Icons.AutoMirrored.Rounded.TrendingUp
        "property", "assets" -> Icons.Rounded.AccountBalance

        else -> Icons.Rounded.Info
    }
}
