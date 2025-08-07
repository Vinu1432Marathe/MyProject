package com.vinayak.semicolon.securefolderhidefiles.VideoData

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.vinayak.semicolon.securefolderhidefiles.Model.VideoModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class VideoRepository(private val context: Context) {

    fun getAllVideos(): List<VideoModel> {
        val videoList = mutableListOf<VideoModel>()
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE
        )

        context.contentResolver.query(
            uri, projection, null, null,
            "${MediaStore.Video.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val name = cursor.getString(nameCol)
                val size = cursor.getLong(sizeCol)
                val contentUri = ContentUris.withAppendedId(uri, id)

                videoList.add(VideoModel(contentUri, name, size))
            }
        }

        return videoList
    }

    fun moveVideosToHiddenFolder(videos: List<VideoModel>, path: String): Boolean {
        val targetDir = File(path)
        if (!targetDir.exists()) targetDir.mkdirs()

        var success = true

        for (video in videos) {
            try {
                val inputStream = context.contentResolver.openInputStream(video.uri) ?: continue
                val targetFile = File(targetDir, video.name)
                val outputStream = FileOutputStream(targetFile)
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()
                saveOriginalPath(video.name, video.uri.toString())
                // ✅ Delete original file using MediaStore
                context.contentResolver.delete(video.uri, null, null)

                // ✅ Optionally scan new file to update MediaStore
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(targetFile.absolutePath),
                    null,
                    null
                )
            } catch (e: Exception) {
                success = false
                e.printStackTrace()
            }
        }

        return success
    }


    fun getVideosFromHiddenFolder(folderPath: String): List<VideoModel> {
        val folder = File(folderPath)
        val videoList = mutableListOf<VideoModel>()

        if (folder.exists() && folder.isDirectory) {
            folder.listFiles()?.forEach { file ->
                if (file.isFile && file.extension.lowercase() in listOf("mp4", "mkv", "avi", "mov")) {
                    videoList.add(
                        VideoModel(
                            uri = Uri.fromFile(file),
                            name = file.name,
                            size = file.length(),
                            isSelected = false
                        )
                    )
                }
            }
        }

        return videoList
    }
    private fun saveOriginalPath(fileName: String, originalUri: String) {
        val prefs = context.getSharedPreferences("VideoRestorePrefs", Context.MODE_PRIVATE)
        prefs.edit().putString(fileName, originalUri).apply()
    }

    private fun getOriginalPath(fileName: String): String? {
        val prefs = context.getSharedPreferences("VideoRestorePrefs", Context.MODE_PRIVATE)
        return prefs.getString(fileName, null)
    }
    fun restoreVideo(video: VideoModel): Boolean {
        val currentFile = File(video.uri.path ?: return false)
        val originalUriStr = getOriginalPath(video.name) ?: return false

        try {
            val mimeType = when {
                video.name.endsWith(".mp4", true) -> "video/mp4"
                video.name.endsWith(".mkv", true) -> "video/x-matroska"
                else -> "video/*"
            }

            val contentValues = ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, video.name)
                put(MediaStore.Video.Media.MIME_TYPE, mimeType)
                put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES)
            }

            val resolver = context.contentResolver
            val newUri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?: return false

            val inputStream = FileInputStream(currentFile)
            val outputStream = resolver.openOutputStream(newUri) ?: return false
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()

            // Delete from hidden folder
            currentFile.delete()

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
    fun moveVideosToFolder(videos: List<VideoModel>, targetPath: String): Boolean {
        val targetDir = File(targetPath)
        if (!targetDir.exists()) targetDir.mkdirs()

        var success = true

        for (video in videos) {
            try {
                val inputStream = context.contentResolver.openInputStream(video.uri) ?: continue
                val destFile = File(targetDir, video.name)
                val outputStream = FileOutputStream(destFile)

                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()

                // ✅ Step 1: Properly delete from MediaStore
                deleteFromMediaStore(video.uri)

                // ✅ Step 2: Rescan new file
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(destFile.absolutePath),
                    null,
                    null
                )

            } catch (e: Exception) {
                e.printStackTrace()
                success = false
            }
        }

        return success
    }

    private fun deleteFromMediaStore(uri: Uri): Boolean {
        return try {
            val rows = context.contentResolver.delete(uri, null, null)
            rows > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }



}
