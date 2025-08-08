package com.vinayak.semicolon.securefolderhidefiles.Room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: NoteRepository
    val notes: LiveData<List<Note>>

    init {
        val dao = NoteDatabase.getDatabase(application).noteDao()
        repo = NoteRepository(dao)
        notes = repo.allNotes
    }

    fun insert(note: Note) = viewModelScope.launch { repo.insert(note) }
    fun update(note: Note) = viewModelScope.launch { repo.update(note) }
    fun delete(note: Note) = viewModelScope.launch { repo.delete(note) }
}