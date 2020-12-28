package br.com.pebmed.domain.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Date.toCacheFormat(): String {
    return toSupportedDateFormat(SupportedDateFormat.CACHE)
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}

fun String.toDate(supportedDateFormat: SupportedDateFormat): Date {
    val serverDateFormat = SimpleDateFormat(supportedDateFormat.pattern, Locale.US)

    return try {
        serverDateFormat.parse(this)
    } catch (e: Exception) {
        Calendar.getInstance().time
    }
}

fun Date.toSupportedDateFormat(supportedDateFormat: SupportedDateFormat): String {
    val dateFormat = SimpleDateFormat(supportedDateFormat.pattern, Locale.US)

    return dateFormat.format(this)
}

enum class SupportedDateFormat(val pattern: String) {
    SERVER("EEE MMM dd HH:mm:ss zzz yyyy"),
    CACHE("dd/M/yyyy hh:mm:ss"),
    US("yyyy-MM-dd")
}