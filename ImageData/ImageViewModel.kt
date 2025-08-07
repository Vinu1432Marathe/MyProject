package com.vinayak.semicolon.securefolderhidefiles.ImageData

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinayak.semicolon.securefolderhidefiles.Model.ImageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class ImageViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ImageRepository(application)
    private val _images = MutableLiveData<List<ImageModel>>()
    val images: LiveData<List<ImageModel>> = _images

    public val selectedItems = mutableListOf<ImageModel>()

    fun loadAllImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val imageList = repository.getAllImages()
            withContext(Dispatchers.Main) {
                _images.value = imageList
            }
        }
    }

    fun loadFromHiddenFolder(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageList = repository.getImagesFromHiddenFolder(path)
            withContext(Dispatchers.Main) {
                _images.value = imageList
            }
        }
    }

    fun moveSelectedToHidden(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val selected = _images.value?.filter { it.isSelected } ?: emptyList()
            if (repository.moveImagesToHiddenFolder(selected, path)) {
                loadAllImages()
            }
        }
        loadAllImages()
    }

    fun moveSelectedToDelete(path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val selected = _images.value?.filter { it.isSelected } ?: emptyList()
            if (repository.moveImagesToHiddenFolder(selected, path)) {
                loadFromHiddenFolder(path)
            }
        }
        loadFromHiddenFolder(path)
    }

    fun toggleSelection(audio: ImageModel) {
        if (audio.isSelected) {
            selectedItems.add(audio)
        } else {
            selectedItems.remove(audio)
        }
    }

    fun toggleSelectClearAll() {
        _images.value?.let { list ->
            val allSelected = list.all { it.isSelected }
            if (allSelected) {
                list.forEach { it.isSelected = false }
                selectedItems.clear()
            } else {
                list.forEach { it.isSelected = true }
                selectedItems.clear()
                selectedItems.addAll(list)
            }
            _images.postValue(list)
        }
    }

    fun deleteSelectedImages(hiddenPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedItems.forEach {
                repository.deleteImageFile(it)
            }
            selectedItems.clear()
            loadFromHiddenFolder(hiddenPath)
        }
    }

    fun restoreSelectedImages(hiddenPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedItems.forEach { repository.restoreImage(it) }
            selectedItems.clear()
            loadHiddenImages(hiddenPath)
        }
        loadHiddenImages(hiddenPath)
    }

    fun loadHiddenImages(path: String) {
//        val hiddenPath = "/storage/emulated/0/Download/.HideSecureFolder/FolderImage/nsdj"
        val folder = File(path)
        val imageList = mutableListOf<ImageModel>()

        if (folder.exists() && folder.isDirectory) {
            folder.listFiles()?.forEach { file ->
                if (file.isFile && isImageFile(file)) {
                    imageList.add(
                        ImageModel(
                            uri = Uri.fromFile(file),
                            name = file.name,
                            size = file.length()
                        )
                    )
                }
            }
        }

        _images.value = imageList
    }

    private fun isImageFile(file: File): Boolean {
        val extensions = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
        return extensions.any { file.name.endsWith(it, ignoreCase = true) }
    }

    fun moveDocumentsBetweenFolders(list: List<ImageModel>, targetPath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.moveImagesToAnotherFolder(list, targetPath)
        }
    }

}

