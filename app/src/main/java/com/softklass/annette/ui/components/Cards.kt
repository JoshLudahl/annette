package com.softklass.annette.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material.icons.rounded.Money
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softklass.annette.core.ui.currency.currencyFormatter
import com.softklass.annette.data.database.dao.BalanceSheetItemWithValue
import com.softklass.annette.data.model.BalanceSheetType
import com.softklass.theme.ui.theme.ExtendedTheme
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BalanceSheetHeaderCard(
    items: List<BalanceSheetItemWithValue>,
    type: BalanceSheetType,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClickIcon: () -> Unit
) {
    val colors: Pair<Color, Color> = when (type) {
        BalanceSheetType.ASSETS -> ExtendedTheme.colors.asset.color to ExtendedTheme.colors.asset.onColor
        BalanceSheetType.LIABILITIES -> ExtendedTheme.colors.liability.colorContainer to ExtendedTheme.colors.liability.onColorContainer
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),

        colors = CardDefaults.cardColors(
            containerColor = colors.first.copy(alpha = 0.4f),
        ),
        shape = RoundedCornerShape(24.dp)
    ) {

        val label = when (type) {
            BalanceSheetType.ASSETS -> "Assets"
            BalanceSheetType.LIABILITIES -> "Liabilities"
        }

        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),

            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                RoundedIconDisplay(
                    icon = icon,
                    iconContainerColor = colors.first,
                    iconContentColor = colors.second,
                    onClickIcon = onClickIcon
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = currencyFormatter.format(items.sumOf { it.value ?: 0.0 }),
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp
                )
                Text(
                    text = "Total $label",
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
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

    val itemColor = when (item.type) {
        "asset" -> ExtendedTheme.colors.asset.colorContainer
        "liability" -> ExtendedTheme.colors.liability.colorContainer
        else -> ExtendedTheme.colors.asset.colorContainer
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongPress
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(65.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = itemColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = resolveIconForCategory(item.category),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = itemColor
                )
            }


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
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }
}


@Composable
fun CallToActionCard(
    title: String,
    subtitle: String,
    showText: Boolean = true,
    monthlyBudget: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit = { /* no-op */ }
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        shape = RoundedCornerShape(24.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor.copy(alpha = 0.6f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Thin
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                if (showText) {
                    Text(
                        text = monthlyBudget,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = contentColor
                    )
                } else {
                    RoundedIconDisplay(
                        icon = Icons.Rounded.MonetizationOn,
                        iconContainerColor = ExtendedTheme.colors.blackboard.color,
                    )
                }
            }
        }
    }
}


@Composable
fun BalanceSheetRoundedCardContainer(
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            // Apply clipping to the Card itself
            .clip(
                RoundedCornerShape(
                    topStart = 24.dp, // Adjust the radius as needed
                    topEnd = 24.dp,   // Adjust the radius as needed
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            ),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            content()
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
        "credit card", "card", "credit" -> Icons.Rounded.CreditCard
        "loan" -> Icons.Rounded.AttachMoney
        "cash" -> Icons.Rounded.Money
        "investments", "retirement" -> Icons.AutoMirrored.Rounded.TrendingUp
        "property", "assets" -> Icons.Rounded.AccountBalance

        else -> Icons.Rounded.Info
    }
}
