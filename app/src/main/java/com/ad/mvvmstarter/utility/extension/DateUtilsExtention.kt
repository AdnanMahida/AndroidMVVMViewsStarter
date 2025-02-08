package com.ad.mvvmstarter.utility.extension

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.toYYYYMMDDDate(): Date {
    return try {
        SimpleDateFormat(
            "yyyy/MM/dd", Locale.getDefault()
        ).parse(this)!!
    } catch (ex: Exception) {
        Calendar.getInstance().time
    }
}

fun String.toMMDDYYYYDate(): Date {
    return try {
        SimpleDateFormat(
            "MM/dd/yyyy", Locale.getDefault()
        ).parse(this)!!
    } catch (ex: Exception) {
        Calendar.getInstance().time
    }
}

fun Date.toYYYYMMDDSlash(): String {
    val format = SimpleDateFormat(
        "yyyy/MM/dd", Locale.getDefault()
    )
    return format.format(this)
}

fun Date.toMMDDYYYYSlash(): String {
    val format = SimpleDateFormat(
        "MM/dd/yyyy", Locale.getDefault()
    )
    return format.format(this)
}

fun Date.toYYYYMMDDDash(): String {
    val format = SimpleDateFormat(
        "yyyy-MM-dd", Locale.getDefault()
    )
    return format.format(this)
}

fun String.toYYYYMMDD(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val date = inputFormat.parse(this)
    return outputFormat.format(date)
}

fun String.fromYYYYMMDDToMMDDYYYY(): String {
    val theDateFormat: DateFormat = SimpleDateFormat(
        "yyyy/MM/dd", Locale.getDefault()
    )
    var date = Date()
    try {
        date = theDateFormat.parse(this) as Date
    } catch (exception: Exception) {
        exception.printStackTrace()
    }
    val format = SimpleDateFormat(
        "MM/dd/yyyy", Locale.getDefault()
    )
    return format.format(date)
}

fun String.getDateFromTimeZone(): String {
    val sdf = SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss z", Locale.getDefault()
    )
    sdf.timeZone = TimeZone.getTimeZone(this)
    return sdf.format(Date())
}

fun getYYYYMMDDDate(): String {
    val currentDate = Date()
    val formatter = SimpleDateFormat(
        "yyyy-MM-dd", Locale.getDefault()
    )
    return formatter.format(currentDate)
}

fun getYYYYMMDDUTCDate(): String {
    val utcTime = ZonedDateTime.now(ZoneOffset.UTC)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return utcTime.format(formatter)
}

fun getCurrentTime(): String {
    val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    return dateFormat.format(Date()).uppercase(Locale.getDefault())
}

fun getCurrentUtcTime(): String {
    val dateFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
    println(dateFormatter.format(Date()))
    return dateFormatter.format(Date())
}

fun getCurrentPTSTime(): String {
    val dateFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    dateFormatter.timeZone = TimeZone.getTimeZone("America/Vancouver")
    println(dateFormatter.format(Date()))
    return dateFormatter.format(Date())
}

fun getCurrentUtcFullDate(): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
    dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormatter.format(Date())
}