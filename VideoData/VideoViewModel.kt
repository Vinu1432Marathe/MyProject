package com.vinayak.semicolon.securefolderhidefiles.VideoData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinayak.semicolon.securefolderhidefiles.Model.DocumentModel
import com.vinayak.semicolon.securefolderhidefiles.Model.VideoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class VideoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = VideoRepository(application)
    private val _videos = MutableLiveData<List<VideoModel>>()
    val videos: LiveData<List<VideoModel>> = _videos
    public val selectedItems = mutableListOf<VideoModel>()

    fun loadAllVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            val videoList = repository.getAllVideos()
            withContext(Dispatchers.Main) {
                _videos.value = videoList
            }
        }
    }

    fun loadHiddenVideos(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val hiddenVideos = repository.getVideosFromHiddenFolder(path)
            withContext(Dispatchers.Main) {
                _videos.value = hiddenVideos
            }
        }
    }


    fun moveSelectedToHidden(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val selected = _videos.value?.filter { it.isSelected } ?: emptyList()
            if (repository.moveVideosToHiddenFolder(selected, path)) {
                loadAllVideos()
            }
        }
        loadAllVideos()
    }

    fun moveSelectedToDelete(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val selected = _videos.value?.filter { it.isSelected } ?: emptyList()
            if (repository.moveVideosToFolder(selected, path)) {
                loadHiddenVideos(path)
            }
        }
        loadHiddenVideos(path)
    }

    fun toggleSelection(video: VideoModel) {
        if (video.isSelected) {
            selectedItems.add(video)
        } else {
            selectedItems.remove(video)
        }
    }

    fun toggleSelectClearAll() {
        _videos.value?.let { list ->
            val allSelected = list.all { it.isSelected }
            if (allSelected) {
                list.forEach { it.isSelected = false }
                selectedItems.clear()
            } else {
                list.forEach { it.isSelected = true }
                selectedItems.clear()
                selectedItems.addAll(list)
            }
            _videos.postValue(list)
        }
    }

    fun restoreSelectedVideos(hiddenPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedItems.forEach {
                repository.restoreVideo(it)
            }
            selectedItems.clear()
            loadHiddenVideos(hiddenPath)
        }
    }

    fun moveSelectedVideosToFolder(list: List<VideoModel>,targetPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.moveVideosToFolder(list, targetPath)
        }
    }

}



