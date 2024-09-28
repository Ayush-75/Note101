package com.example.note101.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.note101.data.NotesDataBase
import com.example.note101.data.SortOrder
import com.example.note101.data.models.NotesData
import com.example.note101.data.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val notesDao = NotesDataBase.getDataBase(application).notesDao()
    private val repository: NotesRepository = NotesRepository(notesDao)

    val getAllData: LiveData<List<NotesData>> = repository.getAllData

    private val sortOrder = MutableLiveData<SortOrder>(SortOrder.BY_DATE)

    val allNotes: LiveData<List<NotesData>> = sortOrder.switchMap { order ->
        when (order) {
            SortOrder.BY_DATE -> repository.getAllData
            SortOrder.BY_PRIORITY_HIGH -> repository.sortByHighPriority
            SortOrder.BY_PRIORITY_LOW -> repository.sortByLowPriority
            null -> repository.getAllData
        }
    }

    fun setSortOrder(order: SortOrder){
        sortOrder.value = order
    }


    fun insertData(notesData: NotesData) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertData(notesData)
    }

    fun update(notesData: NotesData) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateData(notesData)
    }

    fun delete(notesData: NotesData) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteData(notesData)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<NotesData>> = repository.searchDatabase(searchQuery)

    val sortByHighPriority: LiveData<List<NotesData>> = repository.sortByHighPriority

    val sortByLowPriority: LiveData<List<NotesData>> = repository.sortByLowPriority
}