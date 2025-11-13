package com.softklass.annette.core.ui

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
