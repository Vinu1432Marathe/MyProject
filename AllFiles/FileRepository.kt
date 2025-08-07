package com.vinayak.semicolon.securefolderhidefiles.AllFiles

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import com.vinayak.semicolon.securefolderhidefiles.Model.AudioModel
import com.vinayak.semicolon.securefolderhidefiles.Model.FileModel
import com.vinayak.semicolon.securefolderhidefiles.Model.FolderModel
import java.io.File

// --- FileRepository.kt ---
class FileRepository(private val context: Context) {
    private val imageExts = listOf("jpg", "jpeg", "png", "webp")
    private val videoExts = listOf("mp4", "mkv", "avi", "3gp")
    private val audioExts = listOf("mp3", "wav", "aac", "flac", "ogg", "m4a")
    private val docExts = listOf("pdf", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "txt")

    fun getFoldersWithMediaFiles(): List<FolderModel> {
        val rootDir = Environment.getExternalStorageDirectory()
        val folders = mutableListOf<FolderModel>()

        rootDir.listFiles()?.filter { it.isDirectory }?.forEach { folder ->
            val mediaFiles = folder.listFiles()?.filter { file ->
                val ext = file.extension.lowercase()
                file.isFile && (ext in imageExts || ext in videoExts || ext in audioExts || ext in docExts)
            }?.map {
                FileModel(
                    name = it.name,
                    path = it.absolutePath,
                    size = it.length(),
                    isDirectory = false
                )
            } ?: emptyList()

            folders.add(
                FolderModel(
                    folderName = folder.name,
                    folderPath = folder.absolutePath,
                    files = mediaFiles
                )
            )
        }

        return folders
    }

    fun moveFilesToHiddenFolder(files: List<FileModel>, hiddenPath: String): Boolean {
        val hiddenFolder = File(hiddenPath)
        if (!hiddenFolder.exists()) hiddenFolder.mkdirs()

        var success = true

        files.forEach { model ->
            val src = File(model.path)
            if (src.exists()) {
                val originalFolderName = File(model.path).parentFile?.name ?: "Unknown"
                val destFileName = "${originalFolderName}_${src.name}"
                val dest = File(hiddenFolder, destFileName)

                try {
                    src.copyTo(dest, overwrite = true)
                    src.delete()

                    MediaScannerConnection.scanFile(
                        context, arrayOf(dest.absolutePath), null, null
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    success = false
                }
            }
        }

        return success
    }
    fun restoreFilesFromHiddenFolder(hiddenPath: String): Boolean {
        val hiddenFolder = File(hiddenPath)
        if (!hiddenFolder.exists()) return false

        var success = true

        hiddenFolder.listFiles()?.forEach { file ->
            val split = file.name.split("_", limit = 2)
            if (split.size == 2) {
                val originalFolderName = split[0]
                val originalFileName = split[1]

                val originalFolder = File(Environment.getExternalStorageDirectory(), originalFolderName)
                if (!originalFolder.exists()) originalFolder.mkdirs()

                val dest = File(originalFolder, originalFileName)

                try {
                    file.copyTo(dest, overwrite = true)
                    file.delete()

                    MediaScannerConnection.scanFile(
                        context, arrayOf(dest.absolutePath), null, null
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    success = false
                }
            }
        }

        return success
    }



    fun getFilesFromPath(path: String): List<FileModel> {
        val targetDir = File(path)
        return targetDir.listFiles()?.filter { it.isFile }?.map {
            FileModel(
                name = it.name,
                path = it.absolutePath,
                size = it.length(),
                isDirectory = false
            )
        } ?: emptyList()
    }

//    fun deleteAudioFile(modelFile: FileModel): Boolean {
//        val file = File(modelFile.path)
//        return file.exists() && file.delete()
//    }

    fun deleteAudioFile(modelFile: FileModel): Boolean {
        val file = File(modelFile.path)
        val deleted = file.exists() && file.delete()

        if (deleted) {
            MediaScannerConnection.scanFile(context, arrayOf(modelFile.path), null, null)
        }

        return deleted
    }


}
