package com.example.note101.data.repository

import androidx.lifecycle.LiveData
import com.example.note101.data.NotesDao
import com.example.note101.data.models.NotesData

class NotesRepository(private val notesDao: NotesDao) {

    val getAllData: LiveData<List<NotesData>> = notesDao.getAllData()

    suspend fun insertData(notesData: NotesData) = notesDao.insertData(notesData)

    suspend fun updateData(notesData: NotesData) = notesDao.updateData(notesData)

    suspend fun deleteData(notesData: NotesData) = notesDao.deleteData(notesData)

    suspend fun deleteAll() = notesDao.deleteAll()

    fun searchDatabase(searchQuery: String): LiveData<List<NotesData>> = notesDao.searchDatabase(searchQuery)

    fun sortByHighPriority(): LiveData<List<NotesData>> = notesDao.sortByHighPriority()

    fun sortByLowPriority(): LiveData<List<NotesData>> = notesDao.sortByLowPriority()


}