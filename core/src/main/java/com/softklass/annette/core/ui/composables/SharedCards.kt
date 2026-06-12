package com.softklass.annette.core.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.CurrencyBitcoin
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.automirrored.rounded.TrendingDown
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softklass.annette.core.model.Budget
import com.softklass.annette.core.ui.currency.currencyFormatter
import com.softklass.theme.ui.theme.ExtendedTheme

@Composable
fun DisplayIncomeExpenseCards(
    income: Double,
    expense: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ValueCard(
            totalAssets = income,
            title = "Income",
            textColor = ExtendedTheme.colors.blackboard.color,
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.AccountBalance,
            iconBackgroundColor = ExtendedTheme.colors.asset.colorContainer,
            onCardClick = { /* Handle income card click */ }
        )
        ValueCard(
            totalAssets = expense,
            title = "Expense",
            textColor = ExtendedTheme.colors.blackboard.color,
            modifier = Modifier.weight(1f),
            icon = Icons.Rounded.AccountBalanceWallet,
            iconBackgroundColor = ExtendedTheme.colors.liability.colorContainer,
            onCardClick = { /* Handle expense card click */ }
        )
    }
}

@Composable
fun ValueCard(
    totalAssets: Double,
    title: String,
    textColor: Color,
    modifier: Modifier,
    icon: ImageVector = Icons.Filled.AccountBalance,
    iconBackgroundColor: Color,
    onCardClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = iconBackgroundColor.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            RoundedIconDisplay(
                icon = icon,
                iconContainerColor = iconBackgroundColor,
                onClickIcon = onCardClick
            )

            Spacer(modifier = Modifier.height(45.dp))

            Text(
                text = currencyFormatter.format(totalAssets),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Thin,
                color = textColor.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
fun BudgetInfoHeader(
    total: Double,
    type: Budget? = null
) {
    val item = when (type) {
        Budget.INCOME -> "Total Income" to ExtendedTheme.colors.asset.colorContainer
        Budget.EXPENSE -> "Total Expense" to ExtendedTheme.colors.liability.colorContainer
        else -> "Income - Expense" to ExtendedTheme.colors.cta.color
    }

    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = item.second.copy(alpha = 0.2f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RoundedIconDisplay(
                    icon = when (type) {
                        Budget.INCOME -> Icons.Rounded.AccountBalance
                        Budget.EXPENSE -> Icons.Rounded.AccountBalanceWallet
                        else -> Icons.Rounded.Info
                    },
                    iconContainerColor = item.second,
                    onClickIcon = { }
                )

                Spacer(modifier = Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = currencyFormatter.format(total),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = item.first,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

/**
 * A reusable crypto asset card displaying a holding's name, amount, fiat value, and
 * percentage change. A circular icon badge is anchored at the top-right corner using
 * a custom concave card shape that accommodates the badge.
 *
 * @param cryptoName          Display name of the asset (e.g. "Bitcoin").
 * @param cryptoAmount        Amount held as a formatted string (e.g. "3.689087").
 * @param fiatValue           Equivalent fiat value as a formatted string (e.g. "$98,160").
 * @param percentageChange    24-hour change as a float (e.g. -18f or 5.4f).
 * @param icon                Icon representing the asset (e.g. Icons.Rounded.CurrencyBitcoin).
 * @param modifier            Optional modifier for the outer container.
 * @param cardColor           Background color of the card. Defaults to near-black.
 * @param iconBackgroundColor Background of the circular icon badge. Defaults to light grey.
 * @param iconTint            Tint applied to the icon. Defaults to black.
 * @param iconSize            Diameter of the circular icon badge.
 * @param onCardClick         Callback invoked when the card is clicked.
 */
@Composable
fun CryptoAssetCard(
    cryptoName: String,
    cryptoAmount: String,
    fiatValue: String,
    percentageChange: Float,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    cardColor: Color = Color(0xFF0D0D0D),
    iconBackgroundColor: Color = Color(0xFFEEEEEE),
    iconTint: Color = Color.Black,
    iconSize: Dp = 64.dp,
    onCardClick: () -> Unit = {}
) {
    val density = LocalDensity.current
    val cutoutRadiusPx = with(density) { (iconSize / 2 + 4.dp).toPx() }
    val cornerRadiusPx = with(density) { 28.dp.toPx() }

    val cardShape = remember(cutoutRadiusPx, cornerRadiusPx) {
        GenericShape { size, _ ->
            // Start after top-left corner
            moveTo(cornerRadiusPx, 0f)
            // Top edge up to the concave cutout
            lineTo(size.width - cutoutRadiusPx, 0f)
            // Concave arc: circle centred at (width, 0), sweeps 180° → 270°
            arcTo(
                rect = Rect(
                    left = size.width - cutoutRadiusPx * 2,
                    top = -cutoutRadiusPx,
                    right = size.width,
                    bottom = cutoutRadiusPx
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            // Right edge down to bottom-right corner
            lineTo(size.width, size.height - cornerRadiusPx)
            // Bottom-right rounded corner
            arcTo(
                rect = Rect(
                    left = size.width - cornerRadiusPx * 2,
                    top = size.height - cornerRadiusPx * 2,
                    right = size.width,
                    bottom = size.height
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            // Bottom edge
            lineTo(cornerRadiusPx, size.height)
            // Bottom-left rounded corner
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = size.height - cornerRadiusPx * 2,
                    right = cornerRadiusPx * 2,
                    bottom = size.height
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            // Left edge up to top-left corner
            lineTo(0f, cornerRadiusPx)
            // Top-left rounded corner
            arcTo(
                rect = Rect(
                    left = 0f,
                    top = 0f,
                    right = cornerRadiusPx * 2,
                    bottom = cornerRadiusPx * 2
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            close()
        }
    }

    val isPositive = percentageChange >= 0
    val trendIcon = if (isPositive) Icons.AutoMirrored.Rounded.TrendingUp else Icons.AutoMirrored.Rounded.TrendingDown
    val trendIconTint = if (isPositive) Color(0xFF4CAF50) else Color(0xFFBF8970)
    val percentageText = if (isPositive) "+$percentageChange%" else "$percentageChange%"

    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .padding(top = iconSize / 2)
                .fillMaxWidth(),
            shape = cardShape,
            colors = CardDefaults.cardColors(containerColor = cardColor),
            onClick = onCardClick
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 24.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = percentageText,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = trendIcon,
                        contentDescription = null,
                        tint = trendIconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = cryptoName,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = cryptoAmount,
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = fiatValue,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // Circular icon badge anchored at the top-right corner of the card
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(iconSize)
                .background(color = iconBackgroundColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = cryptoName,
                tint = iconTint,
                modifier = Modifier.size(iconSize / 2)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
private fun CryptoAssetCardNegativePreview() {
    MaterialTheme {
        CryptoAssetCard(
            cryptoName = "Bitcoin",
            cryptoAmount = "3.689087",
            fiatValue = "$98,160",
            percentageChange = -18f,
            icon = Icons.Rounded.CurrencyBitcoin,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
private fun CryptoAssetCardPositivePreview() {
    MaterialTheme {
        CryptoAssetCard(
            cryptoName = "Ethereum",
            cryptoAmount = "12.450000",
            fiatValue = "$39,840",
            percentageChange = 5.4f,
            icon = Icons.Rounded.CurrencyBitcoin,
            iconBackgroundColor = Color(0xFFE8F5E9),
            iconTint = Color(0xFF2E7D32),
            modifier = Modifier.padding(16.dp)
        )
    }
}
