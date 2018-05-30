package com.dcs4u.utils

import java.time.LocalDateTime


fun Map<String, *>.toLocalDateTime(): LocalDateTime {
    val date = this.getMap("date")
    val year = date.toInt("year")
    val month = date.toInt("month")
    val day = date.toInt("day")

    val time = this.getMap("time")
    val hour = time.toInt("hour")
    val minute = time.toInt("minute")
    val second = time.toInt("second")
    val nano = time.toInt("nano")

    return LocalDateTime.of(year, month, day, hour, minute, second, nano)
}

private fun Any.getMap(field: String) = (this as? Map<*, *>)?.get(field) ?: error("Cannot get object for $field")
private fun Any.toInt(field: String): Int {
    val map = this as? Map<*, *>
    val doubleValue = map?.get(field) as? Double ?: error("Cannot get field for $field")
    return doubleValue.toInt()
}