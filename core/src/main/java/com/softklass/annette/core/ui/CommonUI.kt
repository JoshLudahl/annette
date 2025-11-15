package com.softklass.annette.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

fun <E> List<E>.extractProportions(selector: (E) -> Float): List<Float> {
    val total = this.sumOf { selector(it).toDouble() }
    return this.map { (selector(it) / total).toFloat() }
}

@Composable
fun AnnetteDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.10f),
        thickness = 1.dp,
        modifier = modifier
    )
}

@Composable
fun <T> OverViewDivider(
    data: List<T>,
    values: (T) -> Float,
    colors: @Composable (T) -> Color
) {
    // Compute safe weights: ensure strictly positive values to satisfy Modifier.weight() constraints
    val rawValues = data.map { item -> max(0f, values(item)) }
    val total = rawValues.sum()
    // If total is zero (all values are 0 or negative), fall back to equal weights
    val safeWeights = if (total > 0f) {
        rawValues
    } else {
        List(data.size) { 1f }
    }

    Row(Modifier.fillMaxWidth()) {
        data.forEachIndexed { index, item ->
            val weight = safeWeights[index].let { if (it <= 0f) 0.0001f else it }
            Spacer(
                modifier = Modifier
                    .weight(weight)
                    .height(5.dp)
                    .background(colors(item))
            )
        }
    }
}
