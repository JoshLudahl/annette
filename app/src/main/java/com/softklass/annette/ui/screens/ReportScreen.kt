package com.softklass.annette.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.rounded.ShowChart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.softklass.annette.core.ui.design.SegmentedToggle
import com.softklass.annette.core.ui.design.SectionHeader
import com.softklass.annette.core.ui.design.SoftPill
import com.softklass.annette.core.ui.design.categoryIcon
import com.softklass.annette.feature.budget.data.model.BudgetItemType
import com.softklass.annette.feature.budget.ui.screens.BudgetEntity
import com.softklass.annette.feature.budget.ui.screens.BudgetViewModel
import com.softklass.theme.ui.theme.AnnetteTheme
import com.softklass.theme.ui.theme.BrandTheme
import io.github.hdcharts.core.model.toChartData
import io.github.hdcharts.core.style.ChartContainerDefaults
import io.github.hdcharts.gauge.SegmentedRingGaugeChart
import io.github.hdcharts.gauge.SegmentedRingGaugeChartDefaults
import io.github.hdcharts.gauge.SegmentedRingGaugeDirection
import io.github.hdcharts.gauge.SegmentedRingGaugeStartPosition
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

internal data class CategoryTotal(
    val category: String,
    val total: Double,
    val entries: Int,
)

@Composable
fun ReportScreen(
    viewModel: BudgetViewModel,
    modifier: Modifier = Modifier,
) {

    val incomeItems by viewModel.budgetIncomeItems.collectAsState()
    val expenseItems by viewModel.budgetExpenseItems.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpenses by viewModel.totalExpenses.collectAsState()

    ReportContent(
        incomeItems = incomeItems,
        expenseItems = expenseItems,
        incomeTotal = totalIncome,
        expenseTotal = totalExpenses,
        modifier = modifier,
    )
}

@Composable
fun ReportContent(
    incomeItems: List<BudgetEntity>,
    expenseItems: List<BudgetEntity>,
    incomeTotal: Double,
    expenseTotal: Double,
    modifier: Modifier = Modifier,
) {

    var selectedType by remember { androidx.compose.runtime.mutableStateOf(BudgetItemType.EXPENSE) }
    val items = if (selectedType == BudgetItemType.EXPENSE) expenseItems else incomeItems
    val total = if (selectedType == BudgetItemType.EXPENSE) expenseTotal else incomeTotal
    val monthLabel = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US))
    }

    val categoryTotals = remember(items) { items.toCategoryTotals() }
    val chartTotals = remember(categoryTotals) {
        if (categoryTotals.size <= 6) {
            categoryTotals
        } else {
            val top = categoryTotals.take(5)
            val rest = categoryTotals.drop(5)
            top + CategoryTotal(
                category = "Other",
                total = rest.sumOf { it.total },
                entries = rest.sumOf { it.entries },
            )
        }
    }

    val palette = BrandTheme.colors.chart
    val sliceColors = remember(chartTotals, palette) {
        chartTotals.mapIndexed { index, item ->
            if (item.category == "Other") palette.neutral else palette.all[index % palette.all.size]
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
    ) {
        AnnettePageHeader(
            title = "Report",
            subtitle = "Where your money goes",
            actions = {
                SoftPill(text = monthLabel, containerColor = MaterialTheme.colorScheme.surface)
            },
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(Modifier.height(8.dp))
            SegmentedToggle(
                options = listOf(BudgetItemType.EXPENSE, BudgetItemType.INCOME),
                selected = selectedType,
                onSelect = { selectedType = it },
                label = { if (it == BudgetItemType.EXPENSE) "Expenses" else "Income" },
            )
            Spacer(Modifier.height(22.dp))

            SectionHeader(
                title = if (selectedType == BudgetItemType.EXPENSE) "Expenses Report" else "Income Report",
                titleTrailing = {
                    Icon(
                        imageVector = Icons.Rounded.ShowChart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .size(16.dp),
                    )
                },
                actionLabel = if (categoryTotals.isEmpty()) {
                    null
                } else {
                    "${chartTotals.size} ${if (chartTotals.size == 1) "category" else "categories"}"
                },
            )
            Spacer(Modifier.height(14.dp))

            if (chartTotals.isEmpty() || total <= 0.0) {
                GroupedSurface {
                    EmptyState(
                        icon = Icons.Rounded.ShowChart,
                        title = "Nothing to report yet",
                        message = if (selectedType == BudgetItemType.EXPENSE) {
                            "Add an expense from the + button to see your spending breakdown."
                        } else {
                            "Add income from the + button to see where it comes from."
                        },
                    )
                }
            } else {
                GroupedSurface {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 16.dp)
                            .height(236.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        val chartData = remember(chartTotals) {
                            // Categories stay empty: the ring's centre readout owns the labels,
                            // and a category would make the gauge draw its own selection title.
                            chartTotals.map { it.total }.toChartData()
                        }
                        val chartStyle = SegmentedRingGaugeChartDefaults.style(
                            chartContainerStyle = ChartContainerDefaults.style(contentPadding = 0.dp),
                            segments = SegmentedRingGaugeChartDefaults.segments(
                                baseColor = sliceColors.first(),
                                colors = sliceColors,
                                width = 36.dp,
                                spacing = 5.dp,
                                cornerRadius = 14.dp,
                                startPosition = SegmentedRingGaugeStartPosition.Top,
                                direction = SegmentedRingGaugeDirection.Clockwise,
                            ),
                            track = SegmentedRingGaugeChartDefaults.track(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            ),
                            // The gauge's "value" slot is the upper line, so put the caption there
                            // and the amount in the supporting slot to match the reference layout.
                            centerLabels = SegmentedRingGaugeChartDefaults.centerLabels(
                                value = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                                supporting = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                ),
                                spacing = 3.dp,
                            ),
                            legend = SegmentedRingGaugeChartDefaults.legend(visible = false),
                        )
                        SegmentedRingGaugeChart(
                            data = chartData,
                            capacity = total,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            style = chartStyle,
                            centerLabel = if (selectedType == BudgetItemType.EXPENSE) {
                                "Total Expenses"
                            } else {
                                "Total Income"
                            },
                            centerSupportingLabel = currencyFormatter.format(total),
                            interactionEnabled = false,
                            animateOnStart = false,
                        )
                    }
                }

                Spacer(Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (selectedType == BudgetItemType.EXPENSE) "All Expenses" else "All Income",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "Total ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = currencyFormatter.format(total),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Spacer(Modifier.height(12.dp))

                GroupedSurface {
                    chartTotals.forEachIndexed { index, category ->
                        val color = sliceColors[index]
                        val share = if (total > 0) (category.total / total).toFloat() else 0f
                        CategoryReportRow(
                            category = category,
                            color = color,
                            share = share,
                        )
                        if (index != chartTotals.lastIndex) {
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
}

@Composable
private fun CategoryReportRow(
    category: CategoryTotal,
    color: Color,
    share: Float,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 13.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
                    text = "${(share * 100).toInt()}% of total · ${category.entries} ${if (category.entries == 1) "entry" else "entries"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(Modifier.size(8.dp))
            Text(
                text = currencyFormatter.format(category.total),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Spacer(Modifier.height(10.dp))
        GoalProgressBar(
            progress = share,
            color = color,
            trackColor = color.copy(alpha = 0.16f),
            height = 6.dp,
            dashedRemainder = false,
        )
    }
}

internal fun List<BudgetEntity>.toCategoryTotals(): List<CategoryTotal> =
    groupBy { it.category.ifBlank { "Uncategorized" } }
        .map { (category, entries) ->
            CategoryTotal(
                category = category,
                total = entries.sumOf { abs(it.amount) },
                entries = entries.size,
            )
        }
        .filter { it.total > 0.0 }
        .sortedByDescending { it.total }

@Preview(showBackground = true, heightDp = 1200)
@Composable
private fun ReportPreview() {
    val items = listOf(
        BudgetEntity("Groceries", 8750.00, "Groceries", BudgetItemType.EXPENSE, System.currentTimeMillis()),
        BudgetEntity("Clothing", 6420.35, "Shopping", BudgetItemType.EXPENSE, System.currentTimeMillis()),
        BudgetEntity("Rent", 12_000.00, "Housing", BudgetItemType.EXPENSE, System.currentTimeMillis()),
        BudgetEntity("Dining", 4_200.00, "Food", BudgetItemType.EXPENSE, System.currentTimeMillis()),
        BudgetEntity("Transit", 1_800.00, "Transport", BudgetItemType.EXPENSE, System.currentTimeMillis()),
        BudgetEntity("Health", 3_100.00, "Health", BudgetItemType.EXPENSE, System.currentTimeMillis()),
        BudgetEntity("Fun", 2_900.00, "Entertainment", BudgetItemType.EXPENSE, System.currentTimeMillis()),
    )
    AnnetteTheme {
        ReportContent(
            incomeItems = emptyList(),
            expenseItems = items,
            incomeTotal = 0.0,
            expenseTotal = items.sumOf { it.amount },
        )
    }
}
