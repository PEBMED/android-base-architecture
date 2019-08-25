package br.com.pebmed.domain.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.toCacheFormat(): String {
    return toString("dd/M/yyyy hh:mm:ss")
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}