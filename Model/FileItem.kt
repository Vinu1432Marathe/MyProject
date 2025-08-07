package com.vinayak.semicolon.securefolderhidefiles.Model

import android.net.Uri
import java.io.File


data class FileItem(val file: File, val isDirectory: Boolean)

//data class FolderItem(val name: String, val path: String)
data class FolderItem(
    val name: String,
    val path: String,
    val fileCount: Int,
    var isSelected: Boolean = false
)

data class DeviceContact(
    val id: String,
    val name: String,
    val number: String,
    var isSelected: Boolean = false
)

data class AudioModel(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    var data: String,                 // current absolute path
    var isSelected: Boolean = false,  // for multi‑select UI
    var originalPath: String? = null  // <–– set BEFORE moving so we can restore later
)



data class DocumentModel(
    val uri: Uri,
    val name: String,
    val size: Long,
    var isSelected: Boolean = false,
    val data: String? = null
)

data class FolderModel(
    val folderName: String,
    val folderPath: String,
    val files: List<FileModel>
)

data class FileModel(
    val name: String,
    val path: String,
    val size: Long,
    val isDirectory: Boolean,
    var isSelected: Boolean = false,
    val originalPath: String? = null
)

data class VideoModel(
    val uri: Uri,
    val name: String,
    val size: Long,
    var isSelected: Boolean = false,
    val originalUri: String? = null // For restoration
)

data class ImageModel(
    val uri: Uri,
    val name: String,
    val size: Long,
    var isSelected: Boolean = false
)