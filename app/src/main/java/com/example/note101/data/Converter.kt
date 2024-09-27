package com.example.note101.data

import androidx.room.TypeConverter
import com.example.note101.data.models.Priority

class Converter {

    @TypeConverter
    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(name: String): Priority {
        return Priority.valueOf(name)
    }
}