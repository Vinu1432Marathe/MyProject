package com.vinayak.semicolon.securefolderhidefiles.AudioData

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.vinayak.semicolon.securefolderhidefiles.Model.AudioModel
import java.io.File
class AudioRepository(private val context: Context) {

    fun getAllAudioFiles(): List<AudioModel> {
        val audioList = mutableListOf<AudioModel>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val cursor = context.contentResolver.query(uri, projection, selection, null, null)

        cursor?.use {
            val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (it.moveToNext()) {
                val audio = AudioModel(
                    id = it.getLong(idCol),
                    title = it.getString(titleCol),
                    artist = it.getString(artistCol),
                    duration = it.getLong(durationCol),
                    data = it.getString(dataCol)
                )
                audioList.add(audio)
            }
        }

        return audioList
    }

    fun moveAudioToHiddenFolder(audio: AudioModel, targetPath: String): Boolean {
        val sourceFile = File(audio.data)
        if (!sourceFile.exists()) return false

        val targetDir = File(targetPath)
        if (!targetDir.exists()) targetDir.mkdirs()

        val destFile = File(targetDir, sourceFile.name)
        val moved = sourceFile.renameTo(destFile)

        if (moved) {
            Log.e("CheckORGPath","sourceFile.name :: $sourceFile.name")
            Log.e("CheckORGPath","audio.data :: $audio.data")
            saveOriginalPath(sourceFile.name, audio.data)
            // Remove old entry from MediaStore
            context.contentResolver.delete(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Audio.Media.DATA + "=?",
                arrayOf(sourceFile.absolutePath)
            )

            // Add new entry in MediaStore (optional for hidden folder)
            MediaScannerConnection.scanFile(
                context,
                arrayOf(destFile.absolutePath),
                null,
                null
            )
            Log.e("CheckORGPath","sourceFile.name 11 :: $sourceFile.name")
            Log.e("CheckORGPath","audio.data 22 :: $audio.data")
            audio.data = destFile.absolutePath
        }

        return moved
    }

    fun getAudioFilesFromFolder(folderPath: String): List<AudioModel> {
        val folder = File(folderPath)
        if (!folder.exists() || !folder.isDirectory) return emptyList()

        val audioExtensions = listOf(".mp3", ".m4a", ".wav", ".aac", ".ogg")

        val files = folder.listFiles()?.filter { file ->
            audioExtensions.any { ext -> file.name.lowercase().endsWith(ext) }
        } ?: return emptyList()

        return files.mapIndexed { index, file ->
            AudioModel(
                id = index.toLong(),
                title = file.nameWithoutExtension,
                artist = "Unknown",
                duration = 0L,
                data = file.absolutePath
            )
        }
    }

    fun deleteAudioFile(audio: AudioModel): Boolean {
        val file = File(audio.data)
        return file.exists() && file.delete()
    }


    fun restoreAudio(audio: AudioModel): Boolean {
        val currentFile = File(audio.data)
        val originalPath = getOriginalPath(currentFile.name) ?: return false


        val destFile = File(originalPath)
        Log.e("CheckORGPath","Path :: $destFile")
        return if (currentFile.exists() && currentFile.renameTo(destFile)) {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(destFile.absolutePath),
                null,
                null
            )
            audio.data = destFile.absolutePath
            true
        } else false
    }

    private fun saveOriginalPath(key: String, path: String) {
        context.getSharedPreferences("audio_paths", Context.MODE_PRIVATE)
            .edit().putString(key, path).apply()
    }

    private fun getOriginalPath(key: String): String? {
        return context.getSharedPreferences("audio_paths", Context.MODE_PRIVATE)
            .getString(key, null)
    }





    fun getAudioFiles(Path : String): List<AudioModel> {
        val folder = File(Path)

        if (!folder.exists() || !folder.isDirectory) return emptyList()

        val audioExtensions = listOf(".mp3", ".m4a", ".wav", ".aac", ".ogg",".m4a")

        val files = folder.listFiles()?.filter { file ->
            audioExtensions.any { ext -> file.name.lowercase().endsWith(ext) }
        } ?: return emptyList()

        return files.mapIndexed { index, file ->
            AudioModel(
                id = index.toLong(),
                title = file.nameWithoutExtension,
                artist = "Unknown",
                duration = 0L,
                data = file.absolutePath
            )
        }
    }


    fun moveAudioToFolder(audio: AudioModel, targetPath: String): Boolean {
        val sourceFile = File(audio.data)
        if (!sourceFile.exists()) return false

        val targetDir = File(targetPath)
        if (!targetDir.exists()) targetDir.mkdirs()

        val destFile = File(targetDir, sourceFile.name)

        val moved = sourceFile.renameTo(destFile)

        if (moved) {
            context.contentResolver.delete(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Audio.Media.DATA + "=?",
                arrayOf(sourceFile.absolutePath)
            )

            MediaScannerConnection.scanFile(
                context,
                arrayOf(destFile.absolutePath),
                null,
                null
            )

            audio.data = destFile.absolutePath
        }

        return moved
    }




}