package com.example.note101.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.note101.data.models.NotesData

@Database(entities = [NotesData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class NotesDataBase : RoomDatabase() {
    abstract fun notesDao(): NotesDao

    companion object {

        @Volatile
        private var INSTANCE: NotesDataBase? = null

        fun getDataBase(context: Context): NotesDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDataBase::class.java,
                    "notes_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}