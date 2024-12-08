package com.example.wort_des_tages_app.shared

import androidx.room.TypeConverter
import java.sql.Date

class DateConverters {
    @TypeConverter
    fun fromSqlDate(date: Date?): String? {
        return date?.toString() // Converts to SQL DATE string (e.g., "2024-12-07")
    }

    @TypeConverter
    fun toSqlDate(dateString: String?): Date? {
        return dateString?.let { Date.valueOf(it) } // Parses from SQL DATE string
    }
}