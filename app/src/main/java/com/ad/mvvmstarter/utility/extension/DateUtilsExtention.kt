package com.ad.mvvmstarter.utility.extension

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
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
//    outputFormat.timeZone = TimeZone.getTimeZone("America/Vancouver")
    // Parse the input date and format it into the desired output format
    val date = inputFormat.parse(this)
    return outputFormat.format(date)
}

fun String.YYYYMMDDToMMDDYYYY(): String {
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
    // Format the date in "YYYY-MM-DD" format
    val formatter = SimpleDateFormat(
        "yyyy-MM-dd", Locale.getDefault()
    )
    return formatter.format(currentDate)
}

fun getYYYYMMDDUTCDate(): String {
    // Get the current time in UTC
    val utcTime = ZonedDateTime.now(ZoneOffset.UTC)

    // Format the time in a readable format
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


/**
 * Converting date "hh:mm a" into server dateTime String
 * */
fun String.hHMMAToServerDateStr(): String {
    return try {
        // Parse the input time string
        val inputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val parsedTime = inputFormat.parse(this)

        // Get the current date
        val calendar = Calendar.getInstance()
//        val currentDate = calendar.time

        val dateWithTime = Calendar.getInstance()
        if (parsedTime != null) {
            dateWithTime.time = parsedTime
        }
        dateWithTime.set(Calendar.YEAR, calendar.get(Calendar.YEAR))
        dateWithTime.set(Calendar.MONTH, calendar.get(Calendar.MONTH))
        dateWithTime.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH))


        val serverDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        serverDateFormat.timeZone = TimeZone.getTimeZone("UTC")

        return serverDateFormat.format(dateWithTime.time)
    } catch (e: Exception) {
        ""
    }
}


fun formatToServerDate(
    isAnteMeridiem: Boolean, hour: Int, minute: Int, second: Int
): String {
    val calendar = Calendar.getInstance()

    // Set the calendar fields
    calendar.set(Calendar.HOUR, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, second)
    calendar.set(Calendar.MILLISECOND, 0)
    calendar.set(Calendar.AM_PM, if (isAnteMeridiem) Calendar.AM else Calendar.PM)

    // Format to "yyyy-MM-dd'T'HH:mm:ss.SSS"
    val serverDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
    serverDateFormat.timeZone = TimeZone.getTimeZone("UTC")

    return serverDateFormat.format(calendar.time)
}

// finding position for this types of '01:50 AM'  time as PTS
fun List<String>.findInsertPosition(newTime: String): Int {
    // Define the time format (24-hour format)

    val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    val formatter = DateTimeFormatter.ofPattern(pattern)//AppConstants.TIME_DD_MM_PATTERN)

    try {
        // Convert all times in the list to LocalTime objects
        val times = this.map { LocalTime.parse(it, formatter) }.sorted()

        // Convert the new time to LocalTime
        val newTimeParsed = LocalTime.parse(newTime, formatter)

        // Find the position where the new time should be inserted
        val position = times.indexOfFirst { it >= newTimeParsed }

        // If no position is found (meaning the new time is larger than all), return the size of the list
        return if (position == -1) this.size else position
    } catch (e: Exception) {
        // Handle parsing error
        println("Error parsing time: ${e.message}")
        return -1 // Return a default invalid position if there's an error
    }
}