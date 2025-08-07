package com.vinayak.semicolon.securefolderhidefiles.ImageData

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.vinayak.semicolon.securefolderhidefiles.Model.AudioModel
import com.vinayak.semicolon.securefolderhidefiles.Model.ImageModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ImageRepository(private val context: Context) {

    fun getAllImages(): List<ImageModel> {
        val imageList = mutableListOf<ImageModel>()
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )

        context.contentResolver.query(
            uri, projection, null, null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                val name = cursor.getString(nameCol)
                val size = cursor.getLong(sizeCol)
                val contentUri = ContentUris.withAppendedId(uri, id)

                imageList.add(ImageModel(contentUri, name, size))
            }
        }

        return imageList
    }



    fun moveImagesToHiddenFolder(images: List<ImageModel>, path: String): Boolean {
        val targetDir = File(path)
        if (!targetDir.exists()) targetDir.mkdirs()

        var success = true

        for (image in images) {
            try {
                val inputStream = context.contentResolver.openInputStream(image.uri) ?: continue
                val targetFile = File(targetDir, image.name)


                val outputStream = FileOutputStream(targetFile)
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()

                saveOriginalPath(image.name, image.uri.toString())
                // ✅ Delete original file using MediaStore
                context.contentResolver.delete(image.uri, null, null)

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


    fun getImagesFromHiddenFolder(folderPath: String): List<ImageModel> {
        val imageList = mutableListOf<ImageModel>()
        val folder = File(folderPath)

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

        return imageList
    }

    fun deleteImageFile(image: ImageModel): Boolean {
        val file = File(image.uri.path ?: return false)
        return file.exists() && file.delete()
    }

    fun restoreImage(image: ImageModel): Boolean {
        val currentFile = File(image.uri.path ?: return false)
        val originalUriString = getOriginalPath(image.name) ?: return false
        val originalUri = Uri.parse(originalUriString)

        try {
            // Get MIME type from original file extension
            val mimeType = when {
                image.name.endsWith(".jpg", true) || image.name.endsWith(".jpeg", true) -> "image/jpeg"
                image.name.endsWith(".png", true) -> "image/png"
                image.name.endsWith(".gif", true) -> "image/gif"
                else -> "image/*"
            }

            // Insert to MediaStore to create destination file
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, image.name)
                put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val resolver = context.contentResolver
            val newUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?: return false

            // Open input (from hidden folder) and output (new MediaStore file)
            val inputStream = FileInputStream(currentFile)
            val outputStream = resolver.openOutputStream(newUri) ?: return false

            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()

            // Delete file from hidden folder
            currentFile.delete()

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }



    private fun saveOriginalPath(key: String, path: String) {
        context.getSharedPreferences("image_paths", Context.MODE_PRIVATE)
            .edit().putString(key, path).apply()
    }

    private fun getOriginalPath(key: String): String? {
        return context.getSharedPreferences("image_paths", Context.MODE_PRIVATE)
            .getString(key, null)
    }



    private fun isImageFile(file: File): Boolean {
        val supportedExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
        return supportedExtensions.any { file.name.endsWith(it, ignoreCase = true) }
    }

    fun moveImagesToAnotherFolder(images: List<ImageModel>, destinationPath: String): Boolean {
        val targetDir = File(destinationPath)
        if (!targetDir.exists()) targetDir.mkdirs()

        var success = true

        for (image in images) {
            try {
                val inputStream = context.contentResolver.openInputStream(image.uri) ?: continue
                val targetFile = File(targetDir, image.name)

                val outputStream = FileOutputStream(targetFile)
                inputStream.copyTo(outputStream)
                inputStream.close()
                outputStream.close()

                // Delete from original location
                context.contentResolver.delete(image.uri, null, null)

                // Scan destination file to update MediaStore
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(targetFile.absolutePath),
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

}
