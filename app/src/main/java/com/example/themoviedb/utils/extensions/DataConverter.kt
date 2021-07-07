package com.example.themoviedb.utils.extensions

import java.text.SimpleDateFormat
import java.util.*

private const val DATE_TEMPLATE = "EEE, d MMM yyyy"
private const val YEAR_TEMPLATE = "yyyy"

fun Date.convertIntoData(): String = SimpleDateFormat(
    DATE_TEMPLATE, Locale.ENGLISH
).format(this)

fun Date.convertIntoYear(): String = SimpleDateFormat(
    YEAR_TEMPLATE, Locale.ENGLISH
).format(this)
