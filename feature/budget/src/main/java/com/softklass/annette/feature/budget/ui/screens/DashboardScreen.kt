package com.softklass.annette.feature.budget.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softklass.annette.core.ui.composables.DisplayIncomeExpenseCards
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

@Composable
fun DashboardTabContent(viewModel: BudgetViewModel) {
    val income by viewModel.totalIncome.collectAsState()
    val expenses by viewModel.totalExpenses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExpenseIncomeDonut(
            income = income,
            expenses = expenses,
            modifier = Modifier.size(220.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cash section boxes (without the "Cash" label)
        DisplayIncomeExpenseCards(
            income = income,
            expense = expenses,
        )
    }
}

private enum class DonutSegment { INCOME, EXPENSE }

@Composable
private fun ExpenseIncomeDonut(
    income: Double,
    expenses: Double,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 20.dp
) {
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    // Tooltip state
    var showTooltip by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf<DonutSegment?>(null) }

    // Ratio calculations with edge cases handled
    val safeIncome = max(0.0, income)
    val safeExpenses = max(0.0, expenses)
    val total = safeIncome.coerceAtLeast(0.0)
    val ratio = when {
        total == 0.0 && safeExpenses == 0.0 -> 0f
        total == 0.0 && safeExpenses > 0.0 -> 1f // all expense visually
        else -> (safeExpenses / total).toFloat().coerceIn(0f, 1f)
    }

    val animatedRatio by animateFloatAsState(targetValue = ratio, label = "donutRatio")

    // Precompute colors outside Canvas (MaterialTheme access is composable)
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val expenseColor = MaterialTheme.colorScheme.error
    val incomeColor = MaterialTheme.colorScheme.primary

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        // Canvas drawing with tap detection
        Box(modifier = Modifier.size(Modifier.size(0.dp).let { modifier }.let { 0.dp })) {}
        Canvas(
            modifier = Modifier
                .size(modifier
                    .let { 220.dp })
                .pointerInput(income, expenses) {
                    detectTapGestures { offset ->
                        val size = this.size
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val radius = min(size.width, size.height) / 2f
                        val ringInner = radius - strokeWidth.toPx()
                        val dx = offset.x - center.x
                        val dy = offset.y - center.y
                        val distance = hypot(dx, dy)
                        val withinRing = distance in ringInner..radius
                        if (!withinRing) {
                            showTooltip = false
                            return@detectTapGestures
                        }
                        // Angle from -PI..PI, convert to degrees 0..360 starting at -90 (12 o'clock)
                        var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                        angle = (angle + 450f) % 360f // shift so 0° is at top and increases clockwise

                        val expenseSweep = animatedRatio * 360f
                        val tappedSegment = if (angle <= expenseSweep) DonutSegment.EXPENSE else DonutSegment.INCOME
                        selected = tappedSegment
                        showTooltip = true
                    }
                }
        ) {
            val size = this.size
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = min(size.width, size.height) / 2f - 2.dp.toPx()
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)

            // Background track
            drawArc(
                color = trackColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                style = stroke
            )

            // Expense arc (start at -90°)
            val startAngle = -90f
            val expenseSweep = animatedRatio * 360f
            if (expenseSweep > 0.1f) {
                drawArc(
                    color = expenseColor,
                    startAngle = startAngle,
                    sweepAngle = expenseSweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = stroke
                )
            }

            // Income arc is the remainder
            val incomeSweep = 360f - expenseSweep
            if (incomeSweep > 0.1f) {
                drawArc(
                    color = incomeColor,
                    startAngle = startAngle + expenseSweep,
                    sweepAngle = incomeSweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    style = stroke
                )
            }
        }

        // Center info below canvas
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Expenses / Income",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        val percentText = if (income <= 0.0 && expenses > 0.0) ">100%" else "${(ratio * 100).toInt()}%"
        Text(
            text = percentText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))
        if (showTooltip && selected != null) {
            val label = if (selected == DonutSegment.EXPENSE) "Expense" else "Income"
            val amount = if (selected == DonutSegment.EXPENSE) safeExpenses else safeIncome
            DonutTooltip(
                label = label,
                amountText = formatter.format(amount)
            )
        }
    }
}

@Composable
private fun DonutTooltip(label: String, amountText: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 2.dp)
            )
            Text(
                text = amountText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }
    }
}


@Composable
private fun IncomeExpenseBoxes(
    income: Double,
    expense: Double,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryBox(
            title = "Income",
            amount = income,
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.weight(1f)
        )
        SummaryBox(
            title = "Expense",
            amount = expense,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryBox(
    title: String,
    amount: Double,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale.US) }
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = contentColor.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatter.format(amount),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}
