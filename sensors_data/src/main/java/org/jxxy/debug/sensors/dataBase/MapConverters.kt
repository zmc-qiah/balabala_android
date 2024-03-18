package org.jxxy.debug.sensors.dataBase

import androidx.room.TypeConverter

class MapConverters {
    @TypeConverter
    fun fromMap(value: Map<String, String>): String = value.toString()

    @TypeConverter
    fun stringToMap(value: String): Map<String, String> {
        val regex = """\{(.*?)\}""".toRegex()
        val matchResult = regex.find(value)
        val keyValuePairs = matchResult?.groupValues?.get(1)?.split(", ") ?: emptyList()

        val map = mutableMapOf<String, String>()
        for (pair in keyValuePairs) {
            val (key, value) = pair.split("=")
            map[key] = value
        }
        return map
    }
}
