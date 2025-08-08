package com.softklass.annette.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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

    // Group by month-year and sum totals, but only using latest value per day
    val monthYearFormatter = remember { SimpleDateFormat("dd-MMM-yy", Locale.getDefault()) }
    val groupedData = remember(latestPerDay) {
        latestPerDay.groupBy {
            // Convert epoch to month-year string
            monthYearFormatter.format(Date(it.date))
        }.map { (monthYear, values) ->
            monthYear to values.sumOf { it.totalValue }
        }.sortedBy { pair ->
            // Sort by date value parsed from monthYear
            monthYearFormatter.parse(pair.first)?.time ?: 0L
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
                    // Show formatted label if in range. 'value' is Float.
                    val index = value.toInt()
                    if (index in xLabels.indices) xLabels[index] else ""
                }
            ),
        ),
        modelProducer,
    )
}
