package com.softklass.annette.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softklass.annette.core.ui.currency.currencyFormatter
import com.softklass.annette.core.ui.design.AnnettePageHeader
import com.softklass.annette.core.ui.design.CircleIconButton
import com.softklass.annette.core.ui.design.EmptyState
import com.softklass.annette.core.ui.design.GoalProgressBar
import com.softklass.annette.core.ui.design.GroupedSurface
import com.softklass.annette.core.ui.design.PastelIconTile
import com.softklass.annette.core.ui.design.RingProgress
import com.softklass.annette.core.ui.design.SectionHeader
import com.softklass.annette.core.ui.design.categoryIcon
import com.softklass.annette.feature.budget.data.model.BudgetItemType
import com.softklass.annette.feature.budget.ui.components.ConfirmDeleteBudgetItemDialog
import com.softklass.annette.feature.budget.ui.screens.BudgetEntity
import com.softklass.annette.feature.budget.ui.screens.BudgetViewModel
import com.softklass.theme.ui.theme.AnnetteTheme
import com.softklass.theme.ui.theme.BrandTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

@Composable
fun PlanScreen(
    viewModel: BudgetViewModel,
    modifier: Modifier = Modifier,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToReport: () -> Unit = {},
) {

    val incomeItems by viewModel.budgetIncomeItems.collectAsState()
    val expenseItems by viewModel.budgetExpenseItems.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()


    PlanContent(
        incomeItems = incomeItems,
        expenseItems = expenseItems,
        incomeTotal = totalIncome,
        expenseTotal = totalExpenses,
        modifier = modifier,
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToReport = onNavigateToReport,
        onDeleteItem = viewModel::deleteBudgetItem,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanContent(
    incomeItems: List<BudgetEntity>,
    expenseItems: List<BudgetEntity>,
    incomeTotal: Double,
    expenseTotal: Double,
    modifier: Modifier = Modifier,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToReport: () -> Unit = {},
    onDeleteItem: (BudgetEntity) -> Unit = {},
) {

    var pendingDelete by remember { mutableStateOf<BudgetEntity?>(null) }

    val categoryTotals = remember(expenseItems) { expenseItems.toCategoryTotals() }
    val budgets = categoryTotals.take(5)
    val recentEntries = remember(incomeItems, expenseItems) {
        (incomeItems + expenseItems).sortedByDescending { it.dueDateMillis }.take(6)
    }

    val goalProgress = if (incomeTotal > 0.0) (expenseTotal / incomeTotal).toFloat() else 0f
    val overBudget = incomeTotal > 0.0 && expenseTotal > incomeTotal
    val goalColor = when {
        incomeTotal <= 0.0 -> BrandTheme.colors.berry
        overBudget -> BrandTheme.colors.warning
        goalProgress > 0.8f -> BrandTheme.colors.warning
        else -> BrandTheme.colors.positive
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        AnnettePageHeader(
            title = "My Plan",
            subtitle = "Goals and monthly budgets",
            actions = {
                CircleIconButton(
                    icon = Icons.Rounded.Person,
                    onClick = onNavigateToSettings,
                    containerColor = BrandTheme.colors.berrySoft,
                    contentColor = BrandTheme.colors.berry,
                    contentDescription = "Profile and settings",
                )
            },
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(10.dp))
            SectionHeader(title = "Goals")
            Spacer(Modifier.height(12.dp))

            GroupedSurface {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PastelIconTile(
                            icon = Icons.Rounded.Savings,
                            tint = goalColor,
                            container = goalColor.copy(alpha = 0.14f),
                            size = 44.dp,
                            iconSize = 22.dp,
                            shape = RoundedCornerShape(13.dp),
                        )
                        Spacer(Modifier.size(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Monthly spending goal",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = "Keep spending below your income",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = currencyFormatter.format(expenseTotal),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(Modifier.size(6.dp))
                        Text(
                            text = "of ${currencyFormatter.format(incomeTotal)} income",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 3.dp),
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    GoalProgressBar(
                        progress = goalProgress,
                        color = goalColor,
                        trackColor = goalColor.copy(alpha = 0.16f),
                        height = 10.dp,
                    )

                    Spacer(Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Your Progress",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = when {
                                incomeTotal <= 0.0 -> "Set an income target"
                                overBudget -> "${currencyFormatter.format(expenseTotal - incomeTotal)} over"
                                else -> "${currencyFormatter.format(incomeTotal - expenseTotal)} left"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (overBudget) BrandTheme.colors.negative else MaterialTheme.colorScheme.onSurface,
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(BrandTheme.colors.warning, BrandTheme.colors.warningEnd),
                                ),
                            )
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.Info,
                                contentDescription = null,
                                tint = BrandTheme.colors.onWarning,
                                modifier = Modifier.size(15.dp),
                            )
                            Spacer(Modifier.size(8.dp))
                            Text(
                                text = when {
                                    incomeTotal <= 0.0 ->
                                        "Add income to compare it with this month's spending."
                                    overBudget ->
                                        "You're ${((goalProgress - 1f) * 100).toInt()}% over your income this month."
                                    else ->
                                        "You're at ${(goalProgress * 100).toInt()}% of your income this month."
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = BrandTheme.colors.onWarning,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            SectionHeader(
                title = "Budgets",
                actionLabel = "View all",
                onAction = onNavigateToReport,
            )
            Spacer(Modifier.height(12.dp))

            GroupedSurface {
                if (budgets.isEmpty()) {
                    EmptyState(
                        icon = Icons.Rounded.Savings,
                        title = "No budgets yet",
                        message = "Add an expense to start tracking category budgets.",
                    )
                } else {
                    budgets.forEachIndexed { index, category ->
                        val palette = BrandTheme.colors.chart.all
                        val color = palette[index % palette.size]
                        val share = if (expenseTotal > 0) (category.total / expenseTotal).toFloat() else 0f
                        BudgetCategoryRow(
                            category = category,
                            color = color,
                            share = share,
                        )
                        if (index != budgets.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 14.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            SectionHeader(title = "Recent entries")
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Long-press an entry to delete it",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(10.dp))

            GroupedSurface {
                if (recentEntries.isEmpty()) {
                    EmptyState(
                        icon = Icons.Rounded.Savings,
                        title = "Nothing here yet",
                        message = "Use the + button to add your first income or expense.",
                    )
                } else {
                    recentEntries.forEachIndexed { index, entry ->
                        val isIncome = entry.type == BudgetItemType.INCOME
                        val tint = if (isIncome) BrandTheme.colors.positive else BrandTheme.colors.negative
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {},
                                    onLongClick = { pendingDelete = entry },
                                )
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            PastelIconTile(
                                icon = categoryIcon(entry.category.ifBlank { entry.name }),
                                tint = tint,
                                container = tint.copy(alpha = 0.14f),
                                size = 40.dp,
                                iconSize = 19.dp,
                                shape = RoundedCornerShape(12.dp),
                            )
                            Spacer(Modifier.size(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = entry.name.ifBlank { "Untitled" },
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = "${entry.category.ifBlank { "Uncategorized" }} · " +
                                        Instant.ofEpochMilli(entry.dueDateMillis)
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate()
                                            .format(DateTimeFormatter.ofPattern("MMM d", Locale.US)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                            Spacer(Modifier.size(8.dp))
                            Text(
                                text = (if (isIncome) "+" else "-") + currencyFormatter.format(abs(entry.amount)),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isIncome) BrandTheme.colors.positive else BrandTheme.colors.negative,
                            )
                        }
                        if (index != recentEntries.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 14.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(120.dp))
        }
    }

    val pending = pendingDelete
    if (pending != null) {
        ConfirmDeleteBudgetItemDialog(
            itemName = pending.name,
            onDismiss = { pendingDelete = null },
            onConfirmDelete = {
                onDeleteItem(pending)
                pendingDelete = null
            },
        )
    }
}

@Composable
private fun BudgetCategoryRow(
    category: CategoryTotal,
    color: androidx.compose.ui.graphics.Color,
    share: Float,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PastelIconTile(
            icon = categoryIcon(category.category),
            tint = color,
            container = color.copy(alpha = 0.14f),
            size = 42.dp,
            iconSize = 20.dp,
            shape = RoundedCornerShape(13.dp),
        )
        Spacer(Modifier.size(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.category,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "${currencyFormatter.format(category.total)} this month",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.size(10.dp))
        RingProgress(
            progress = share,
            color = color,
            size = 46.dp,
            strokeWidth = 4.dp,
        ) {
            Text(
                text = "${(share * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 1200)
@Composable
private fun PlanPreview() {
    val expenses = listOf(
        BudgetEntity("Rent", 1_850.00, "Housing", BudgetItemType.EXPENSE, System.currentTimeMillis()),
        BudgetEntity("Groceries", 640.00, "Groceries", BudgetItemType.EXPENSE, System.currentTimeMillis()),
        BudgetEntity("Dining", 210.00, "Food", BudgetItemType.EXPENSE, System.currentTimeMillis()),
        BudgetEntity("Transit", 120.00, "Transport", BudgetItemType.EXPENSE, System.currentTimeMillis()),
    )
    AnnetteTheme {
        PlanContent(
            incomeItems = listOf(
                BudgetEntity("Salary", 4_800.00, "Salary", BudgetItemType.INCOME, System.currentTimeMillis()),
            ),
            expenseItems = expenses,
            incomeTotal = 4_800.00,
            expenseTotal = expenses.sumOf { it.amount },
        )
    }
}
