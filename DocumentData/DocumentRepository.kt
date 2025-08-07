package com.vinayak.semicolon.securefolderhidefiles.DocumentData

import android.content.ContentUris
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.vinayak.semicolon.securefolderhidefiles.Model.DocumentModel
import java.io.File
import java.io.FileOutputStream

class DocumentRepository(private val context: Context) {

    fun getAllDocuments(): List<DocumentModel> {
        val list = mutableListOf<DocumentModel>()
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.SIZE
        )
        val mimeTypes = arrayOf("application/pdf", "text/plain", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")

        val selection = mimeTypes.joinToString(" OR ") { "${MediaStore.Files.FileColumns.MIME_TYPE} = ?" }
        val uri = MediaStore.Files.getContentUri("external")

        val cursor = context.contentResolver.query(uri, projection, selection, mimeTypes, null)
        cursor?.use {
            val idCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val sizeCol = it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)

            while (it.moveToNext()) {
                val id = it.getLong(idCol)
                val name = it.getString(nameCol)
                val size = it.getLong(sizeCol)
                val fileUri = ContentUris.withAppendedId(uri, id)
                list.add(DocumentModel(fileUri, name, size))
            }
        }

        return list
    }
    fun getDocumentsFromFolder(path: String): List<DocumentModel> {
        val list = mutableListOf<DocumentModel>()
        val folder = File(path)
        if (folder.exists() && folder.isDirectory) {
            folder.listFiles()?.forEach { file ->
                if (file.isFile && !file.name.startsWith(".")) {
                    list.add(
                        DocumentModel(
                            uri = Uri.fromFile(file), // ⚠️ Uri.fromFile — not content Uri
                            name = file.name,
                            size = file.length()
                        )
                    )
                }
            }
        }
        return list
    }

    fun moveToHiddenFolder(selected: List<DocumentModel>, hiddenDirPath: String) {
        val hiddenDir = File(hiddenDirPath)
        if (!hiddenDir.exists()) hiddenDir.mkdirs()

        selected.forEach { doc ->
            try {
                val inputStream = context.contentResolver.openInputStream(doc.uri) ?: return@forEach
                val originalPath = getFilePathFromUri(doc.uri) ?: return@forEach
                val originalFile = File(originalPath)
                val encodedName = "${originalFile.name}_#_${originalFile.parent.replace("/", "__")}"
                val outputFile = File(hiddenDir, encodedName)

                FileOutputStream(outputFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                context.contentResolver.delete(doc.uri, null, null)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun restoreDocuments(documents: List<DocumentModel>) {
        documents.forEach { doc ->
            try {
                val hiddenFile = File(doc.uri.path ?: return@forEach)
                val parts = doc.name.split("_#_")
                if (parts.size < 2) return@forEach

                val originalName = parts[0]
                val originalFolder = parts[1].replace("__", "/")

                val targetDir = File(originalFolder)
                if (!targetDir.exists()) targetDir.mkdirs()

                val targetFile = File(targetDir, originalName)
                hiddenFile.copyTo(targetFile, overwrite = true)
                hiddenFile.delete()

                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(targetFile.absolutePath),
                    null,
                    null
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Files.FileColumns.DATA)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            if (cursor.moveToFirst()) return cursor.getString(index)
        }
        return null
    }

    fun moveDocumentsBetweenFolders(docs: List<DocumentModel>, targetPath: String) {
        val targetDir = File(targetPath)
        if (!targetDir.exists()) targetDir.mkdirs()

        docs.forEach { doc ->
            try {
                val sourceFile = File(doc.uri.path ?: return@forEach)
                val destFile = File(targetDir, doc.name)

                sourceFile.copyTo(destFile, overwrite = true)
                sourceFile.delete()

                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(destFile.absolutePath),
                    null,
                    null
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}