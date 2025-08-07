package com.vinayak.semicolon.securefolderhidefiles.AudioData

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinayak.semicolon.securefolderhidefiles.Model.AudioModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AudioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AudioRepository(application)

    private val _audioList = MutableLiveData<List<AudioModel>>()
    val audioList: LiveData<List<AudioModel>> = _audioList

    public val selectedItems = mutableListOf<AudioModel>()

    fun loadAudioFiles() {
        _audioList.value = repository.getAllAudioFiles()
    }

    fun loadAudioFiles1(Path: String) {
        val list = repository.getAudioFiles(Path)
        _audioList.postValue(list)  // ✅ Fix for background thread usage
    }

    fun moveSelectedAudiosToHiddenFolder(hiddenPath: String) {
        val updatedList = _audioList.value?.map {
            if (it.isSelected) {
                repository.moveAudioToHiddenFolder(it, hiddenPath)
            }
            it.copy(isSelected = false)
        } ?: emptyList()

        _audioList.value = updatedList

        loadAudioFiles()
    }

    fun loadAudioFromHiddenFolder(hiddenPath: String) {
        _audioList.value = repository.getAudioFilesFromFolder(hiddenPath)
        loadAudioFiles1(hiddenPath)
    }

    fun selectAllAudios() {
        _audioList.value = _audioList.value?.map { it.copy(isSelected = true) }
    }

    fun unselectAllAudios() {
        _audioList.value = _audioList.value?.map { it.copy(isSelected = false) }
    }

    fun toggleSelectClearAll() {
        _audioList.value?.let { list ->
            val allSelected = list.all { it.isSelected }
            if (allSelected) {
                list.forEach { it.isSelected = false }
                selectedItems.clear()
            } else {
                list.forEach { it.isSelected = true }
                selectedItems.clear()
                selectedItems.addAll(list)
            }
            _audioList.postValue(list)
        }
    }

    fun toggleSelection(audio: AudioModel) {
        if (audio.isSelected) {
            selectedItems.add(audio)
        } else {
            selectedItems.remove(audio)
        }
    }

    fun deleteSelected(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedItems.forEach { repository.deleteAudioFile(it) }
            selectedItems.clear()
            loadAudioFiles1(path)
        }
    }


    fun restoreSelected(destPath: String) {

        viewModelScope.launch(Dispatchers.IO) {
            selectedItems.forEach { repository.restoreAudio(it) }
            selectedItems.clear()
            loadAudioFiles1(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath)
        }
        loadAudioFromHiddenFolder(destPath)
    }


    fun moveSelectedAudiosToFolder(destinationPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedItems.forEach {
                repository.moveAudioToFolder(it, destinationPath)
            }
            selectedItems.clear()
            loadAudioFiles1(destinationPath) // Load new folder content after move
        }
        loadAudioFiles1(destinationPath)
    }


}
