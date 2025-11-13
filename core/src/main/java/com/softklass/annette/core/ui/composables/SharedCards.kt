package com.softklass.annette.core.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
