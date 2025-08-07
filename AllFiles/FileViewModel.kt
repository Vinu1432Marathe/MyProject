package com.vinayak.semicolon.securefolderhidefiles.AllFiles

import android.R.attr.path
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinayak.semicolon.securefolderhidefiles.Model.FileModel
import com.vinayak.semicolon.securefolderhidefiles.Model.FolderModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FileRepository(application)

    private val _folders = MutableLiveData<List<FolderModel>>()
    val folders: LiveData<List<FolderModel>> = _folders

    val selectedFiles = MutableLiveData<MutableList<FileModel>>(mutableListOf())

    fun loadFolders() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getFoldersWithMediaFiles()
            _folders.postValue(result)
        }
    }

    private val _filesInFolder = MutableLiveData<List<FileModel>>()
    val filesInFolder: LiveData<List<FileModel>> = _filesInFolder

    fun loadFilesFromFolderPath(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getFilesFromPath(path)
            _filesInFolder.postValue(result)
        }
    }

    fun toggleFileSelection(file: FileModel) {
        val list = selectedFiles.value ?: mutableListOf()
        val existing = list.find { it.path == file.path }

        if (existing != null) {
            list.remove(existing)
        } else {
            list.add(file.copy(originalPath = file.path)) // store original path
        }
        selectedFiles.postValue(list)
    }

    fun moveSelectedFiles(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val moved = repository.moveFilesToHiddenFolder(selectedFiles.value ?: emptyList(), path)
            if (moved) {
                selectedFiles.postValue(mutableListOf())
                loadFolders()
            }
        }
        loadFolders()
    }
    fun moveSelectedFiles11(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val moved = repository.moveFilesToHiddenFolder(selectedFiles.value ?: emptyList(), path)
            if (moved) {
                selectedFiles.postValue(mutableListOf())
                loadFilesFromFolderPath(path)
            }
        }
        loadFilesFromFolderPath(path)
    }

    fun moveSelectedFiles11(list: List<FileModel>, path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val moved = repository.moveFilesToHiddenFolder(list, path)
            if (moved) {
                selectedFiles.postValue(mutableListOf())
                loadFilesFromFolderPath(path)
            }
        }
        loadFilesFromFolderPath(path)
    }

    fun restoreSelectedFiles(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val restored = repository.restoreFilesFromHiddenFolder(path)
            if (restored) {
                selectedFiles.postValue(mutableListOf())
                loadFilesFromFolderPath(path)
            }
        }
        loadFilesFromFolderPath(path)
    }

    fun deleteSelectedFiles(currentFolderPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val filesToDelete = selectedFiles.value ?: return@launch

            var anyDeleted = false
            filesToDelete.forEach { file ->
                val deleted = repository.deleteAudioFile(file)
                if (deleted) anyDeleted = true
            }

            if (anyDeleted) {
                selectedFiles.postValue(mutableListOf())
                loadFilesFromFolderPath(currentFolderPath) // refresh the current folder list
            }
        }
    }


}

