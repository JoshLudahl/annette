package com.softklass.annette

import java.text.NumberFormat
import java.util.Locale
import kotlin.text.replace

val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)

val String.amountFormatted: String get() =
    this.replace(",", "")
    .filter { entry -> entry.isDigit() || entry == '.' }