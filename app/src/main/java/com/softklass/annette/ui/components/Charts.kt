package com.softklass.annette.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.softklass.annette.data.database.dao.HistoricalTotal
import com.softklass.annette.data.database.entities.BalanceSheetValues
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoricalChart(historicalTotals: List<HistoricalTotal>) {

    Log.d("HistoricalChart", "historicalTotals: $historicalTotals")

    // Take only the latest value per day
    val dayFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val latestPerDay = remember(historicalTotals) {
        historicalTotals
            .groupBy { dayFormatter.format(Date(it.date)) }
            .mapValues { entry -> entry.value.maxByOrNull { it.date }!! }
            .values
            .toList()
    }

    // Group by day label and sum totals, but only using latest value per day
    val dayLabelFormatter = remember { SimpleDateFormat("dd-MMM-yy", Locale.getDefault()) }
    val groupedData = remember(latestPerDay) {
        latestPerDay.groupBy {
            dayLabelFormatter.format(Date(it.date))
        }.map { (label, values) ->
            label to values.sumOf { it.totalValue }
        }.sortedBy { pair ->
            dayLabelFormatter.parse(pair.first)?.time ?: 0L
        }
    }

    // Prepare X (indices), Y (sums), and labels
    val xValues = groupedData.indices.map { it.toFloat() }
    val yValues = groupedData.map { it.second.toFloat() }
    val xLabels = groupedData.map { it.first }

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(xValues, yValues) {
            modelProducer.runTransaction {
                lineSeries { series(xValues, yValues) }
            }
    }

    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(
                valueFormatter = { _, value, _ ->
                    val index = value.toInt()
                    if (index in xLabels.indices) xLabels[index] else ""
                }
            ),
        ),
        modelProducer,
    )
}

@Composable
fun ItemHistoricalChart(values: List<BalanceSheetValues>) {
    Log.d("ItemHistoricalChart", "values: $values")

    // Deduplicate by day keeping latest
    val dayKeyFormatter = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val latestPerDay = remember(values) {
        values
            .groupBy { dayKeyFormatter.format(Date(it.date)) }
            .mapValues { entry -> entry.value.maxByOrNull { it.date }!! }
            .values
            .sortedBy { it.date }
            .toList()
    }

    if (latestPerDay.size < 2 || values.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.TrendingUp,
                contentDescription = "No historical data",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(40.dp)
            )

            Text(
                text = "No historical data available.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    } else {
        // Build chart points and labels
        val labelFormatter = remember { SimpleDateFormat("dd-MMM-yy", Locale.getDefault()) }
        val xValues = remember(latestPerDay) { latestPerDay.indices.map { it.toFloat() } }
        val yValues = remember(latestPerDay) { latestPerDay.map { it.value.toFloat() } }
        val xLabels =
            remember(latestPerDay) { latestPerDay.map { labelFormatter.format(Date(it.date)) } }

        val modelProducer = remember { CartesianChartModelProducer() }
        LaunchedEffect(xValues, yValues) {
            modelProducer.runTransaction {
                lineSeries { series(xValues, yValues) }
            }
        }

        CartesianChartHost(
            rememberCartesianChart(
                rememberLineCartesianLayer(),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = { _, value, _ ->
                        val index = value.toInt()
                        if (index in xLabels.indices) xLabels[index] else ""
                    }
                ),
            ),
            modelProducer,
        )
    }
}
