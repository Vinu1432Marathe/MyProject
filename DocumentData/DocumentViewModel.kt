package com.vinayak.semicolon.securefolderhidefiles.DocumentData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinayak.semicolon.securefolderhidefiles.Model.DocumentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DocumentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DocumentRepository(application)
    private val _documents = MutableLiveData<List<DocumentModel>>()
    val documents: LiveData<List<DocumentModel>> = _documents

    fun loadDocuments() {
        viewModelScope.launch(Dispatchers.IO) {
            val docs = repository.getAllDocuments()
            _documents.postValue(docs)
        }
    }

    fun loadDocumentsFromPath(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getDocumentsFromFolder(path)
            _documents.postValue(data)
        }
    }

    fun moveSelectedDocuments(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val selected = _documents.value?.filter { it.isSelected } ?: emptyList()
            repository.moveToHiddenFolder(selected, path)
            loadDocuments()
        }
    }

    fun moveSelectedDocuments11(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val selected = _documents.value?.filter { it.isSelected } ?: emptyList()
            repository.moveToHiddenFolder(selected, path)
            loadDocumentsFromPath(path)
        }
        loadDocumentsFromPath(path)
    }

    fun restoreSelected(hiddenDir: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val selected = _documents.value?.filter { it.isSelected } ?: return@launch
            repository.restoreDocuments(selected)

            loadDocumentsFromPath(hiddenDir)
        }
    }


    fun toggleSelectAll() {
        val current = _documents.value ?: return
        val allSelected = current.all { it.isSelected }
        val updated = current.map { it.copy(isSelected = !allSelected) }
        _documents.postValue(updated)
    }

    fun toggleSelection(position: Int) {
        val updatedList = _documents.value?.toMutableList() ?: return
        updatedList[position].isSelected = !updatedList[position].isSelected
        _documents.postValue(updatedList)
    }

    fun moveDocumentsBetweenFolders(list: List<DocumentModel>, targetPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.moveDocumentsBetweenFolders(list, targetPath)
        }
    }


//    fun moveSelectedDocumentsToFolder(targetPath: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val docs = _documents.value
//            if (docs.isNullOrEmpty()) {
//                Log.e("MoveDocs", "No documents loaded")
//                return@launch
//            }
//
//            val selected = docs.filter { it.isSelected }
//            if (selected.isEmpty()) {
//                Log.e("MoveDocs", "No documents selected")
//                return@launch
//            }
//
//            Log.d("MoveDocs", "Moving ${selected.size} documents to $targetPath")
//
//            repository.moveDocumentsBetweenFolders(selected, targetPath)
//            loadDocumentsFromPath(targetPath)
//        }
//    }


}