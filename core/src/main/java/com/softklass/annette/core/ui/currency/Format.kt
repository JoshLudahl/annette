package com.softklass.annette.core.ui.currency

import java.text.NumberFormat
import java.util.Locale

val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)

val shortCurrencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US).apply {
    maximumFractionDigits = 0
}
val String.amountFormatted: String get() =
    this.replace(",", "")
        .filter { entry -> entry.isDigit() || entry == '.' }
