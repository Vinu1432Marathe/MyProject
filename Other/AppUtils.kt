package com.vinayak.semicolon.securefolderhidefiles.Other

import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.AudioData.AudioAdapter
import com.vinayak.semicolon.securefolderhidefiles.Model.AudioModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

object AppUtils {

     val Main_Folder: String = ".HideSecureFolder"
     fun shareApp(context: Context) {
          val shareIntent = Intent(Intent.ACTION_SEND)
          shareIntent.type = "text/plain"
          shareIntent.putExtra(
               Intent.EXTRA_TEXT,
               "Hey, check out this awesome app! ${
                    context.packageManager.getPackageInfo(
                         context.packageName,
                         0
                    ).applicationInfo?.loadLabel(context.packageManager)
               } https://play.google.com/store/apps/details?id=${context.packageName}"
          )
          context.startActivity(Intent.createChooser(shareIntent, "Share via"))
     }

     fun rateUs(context: Context) {
          val packageName = context.packageName
          val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
          val intent = Intent(Intent.ACTION_VIEW, uri)
          try {
               context.startActivity(intent)
          } catch (e: Exception) {
               context.startActivity(
                    Intent(
                         Intent.ACTION_VIEW,
                         Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                    )
               )
          }
     }


     fun openPrivacy(context: Context) {

//          val intent = Intent(Intent.ACTION_VIEW)
//          val configPref = MyApp.ad_preferences.getRemoteConfig()
//
//          if (configPref?.privacyPolicy?.isNotEmpty() == true) {
//
//               intent.data = Uri.parse(configPref.privacyPolicy)
//               try {
//                    context.startActivity(intent)
//               } catch (e: Exception) {
//               }
//          } else {
//               Toast.makeText(context, "Unable to load!", Toast.LENGTH_SHORT).show()
//          }


     }




}

//class AudioRepository(private val context: Context) {
//
//     fun getAllAudioFiles(): List<AudioModel> {
//          val audioList = mutableListOf<AudioModel>()
//          val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//
//          val projection = arrayOf(
//               MediaStore.Audio.Media._ID,
//               MediaStore.Audio.Media.TITLE,
//               MediaStore.Audio.Media.ARTIST,
//               MediaStore.Audio.Media.DURATION,
//               MediaStore.Audio.Media.DATA
//          )
//
//          val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
//
//          val cursor = context.contentResolver.query(uri, projection, selection, null, null)
//
//          cursor?.use {
//               val idCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
//               val titleCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
//               val artistCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
//               val durationCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
//               val dataCol = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
//
//               while (it.moveToNext()) {
//                    val audio = AudioModel(
//                         id = it.getLong(idCol),
//                         title = it.getString(titleCol),
//                         artist = it.getString(artistCol),
//                         duration = it.getLong(durationCol),
//                         data = it.getString(dataCol)
//                    )
//                    audioList.add(audio)
//               }
//          }
//
//          return audioList
//     }
//
//     fun moveAudioToHiddenFolder(audio: AudioModel, targetPath: String): Boolean {
//          val sourceFile = File(audio.data)
//          if (!sourceFile.exists()) return false
//
//          val targetDir = File(targetPath)
//          if (!targetDir.exists()) targetDir.mkdirs()
//
//          val destFile = File(targetDir, sourceFile.name)
//          val moved = sourceFile.renameTo(destFile)
//
//          if (moved) {
//               // Remove old entry from MediaStore
//               context.contentResolver.delete(
//                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                    MediaStore.Audio.Media.DATA + "=?",
//                    arrayOf(sourceFile.absolutePath)
//               )
//
//               // Add new entry in MediaStore (optional for hidden folder)
//               MediaScannerConnection.scanFile(
//                    context,
//                    arrayOf(destFile.absolutePath),
//                    null,
//                    null
//               )
//
//               audio.data = destFile.absolutePath
//          }
//
//          return moved
//     }
//
//
//     fun getAudioFilesFromFolder(folderPath: String): List<AudioModel> {
//          val folder = File(folderPath)
//          if (!folder.exists() || !folder.isDirectory) return emptyList()
//
//          val audioExtensions = listOf(".mp3", ".m4a", ".wav", ".aac", ".ogg")
//
//          val files = folder.listFiles()?.filter { file ->
//               audioExtensions.any { ext -> file.name.lowercase().endsWith(ext) }
//          } ?: return emptyList()
//
//          return files.mapIndexed { index, file ->
//               AudioModel(
//                    id = index.toLong(),
//                    title = file.nameWithoutExtension,
//                    artist = "Unknown",
//                    duration = 0L,
//                    data = file.absolutePath
//               )
//          }
//     }
//
//}
//
//
//class AudioViewModel(application: Application) : AndroidViewModel(application) {
//
//     private val repository = AudioRepository(application)
//
//     private val _audioList = MutableLiveData<List<AudioModel>>()
//     val audioList: LiveData<List<AudioModel>> = _audioList
//
//     fun loadAudioFiles() {
//          _audioList.value = repository.getAllAudioFiles()
//     }
//
//     fun moveSelectedAudiosToHiddenFolder(hiddenPath: String) {
//          val updatedList = _audioList.value?.map {
//               if (it.isSelected) {
//                    repository.moveAudioToHiddenFolder(it, hiddenPath)
//               }
//               it.copy(isSelected = false)
//          } ?: emptyList()
//
//          _audioList.value = updatedList
//
//          loadAudioFiles()
//     }
//
//     fun loadAudioFromHiddenFolder(hiddenPath: String) {
//          _audioList.value = repository.getAudioFilesFromFolder(hiddenPath)
//
//     }
//}
//class AudioListActivity : AppCompatActivity() {
//
//
//
//     private lateinit var adapter: AudioAdapter
//     private lateinit var recyclerView: RecyclerView
//     private lateinit var rlHideData: RelativeLayout
//     private lateinit var img_AllSelect: ImageView
//     private lateinit var folderPath: String
//     private val viewModel: AudioViewModel by viewModels()
//
//
//     override fun onCreate(savedInstanceState: Bundle?) {
//          super.onCreate(savedInstanceState)
//
//          setContentView(R.layout.activity_audio_list)
//          ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//               val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//               v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//               insets
//          }
//          img_AllSelect = findViewById(R.id.img_AllSelect)
//          rlHideData = findViewById(R.id.rlHideData)
//          val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//
//          folderPath = intent.getStringExtra("folderPath").toString()
//          Log.e("checkkPath","FilePath  :: $folderPath")
//
//
//          adapter = AudioAdapter(emptyList()) {
//               rlHideData.isEnabled = adapter.getSelectedItems().isNotEmpty()
//          }
//
//          recyclerView.layoutManager = LinearLayoutManager(this)
//          recyclerView.adapter = adapter
//
//          rlHideData.setOnClickListener {
//               viewModel.moveSelectedAudiosToHiddenFolder(folderPath)
//               Toast.makeText(this, "Files moved!", Toast.LENGTH_SHORT).show()
//          }
//
//          viewModel.audioList.observe(this, Observer {
//               adapter.updateList(it)
//          })
//
//          requestPermissions()
//     }
//
//     private fun requestPermissions() {
//          val permissionLauncher = registerForActivityResult(
//               ActivityResultContracts.RequestMultiplePermissions()
//          ) { permissions ->
//               val granted = permissions.entries.all { it.value == true }
//               if (granted) {
//                    viewModel.loadAudioFiles()
//               } else {
//                    Toast.makeText(this, "Storage permission is required", Toast.LENGTH_LONG).show()
//               }
//          }
//
//          permissionLauncher.launch(
//               arrayOf(
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//               )
//          )
//     }
//}
//class AudioListActivity : AppCompatActivity() {
//
//
//
//     private lateinit var adapter: AudioAdapter
//     private lateinit var recyclerView: RecyclerView
//     private lateinit var rlHideData: RelativeLayout
//     private lateinit var img_AllSelect: ImageView
//     private lateinit var folderPath: String
//     private val viewModel: AudioViewModel by viewModels()
//
//
//     override fun onCreate(savedInstanceState: Bundle?) {
//          super.onCreate(savedInstanceState)
//
//          setContentView(R.layout.activity_audio_list)
//          ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//               val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//               v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//               insets
//          }
//          img_AllSelect = findViewById(R.id.img_AllSelect)
//          rlHideData = findViewById(R.id.rlHideData)
//          val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//
//          folderPath = intent.getStringExtra("folderPath").toString()
//          Log.e("checkkPath","FilePath  :: $folderPath")
//
//
//          adapter = AudioAdapter(emptyList()) {
//               rlHideData.isEnabled = adapter.getSelectedItems().isNotEmpty()
//          }
//
//          recyclerView.layoutManager = LinearLayoutManager(this)
//          recyclerView.adapter = adapter
//
//          rlHideData.setOnClickListener {
//               viewModel.moveSelectedAudiosToHiddenFolder(folderPath)
//               Toast.makeText(this, "Files moved!", Toast.LENGTH_SHORT).show()
//          }
//
//          viewModel.audioList.observe(this, Observer {
//               adapter.updateList(it)
//          })
//
//          requestPermissions()
//     }
//
//     private fun requestPermissions() {
//          val permissionLauncher = registerForActivityResult(
//               ActivityResultContracts.RequestMultiplePermissions()
//          ) { permissions ->
//               val granted = permissions.entries.all { it.value == true }
//               if (granted) {
//                    viewModel.loadAudioFiles()
//               } else {
//                    Toast.makeText(this, "Storage permission is required", Toast.LENGTH_LONG).show()
//               }
//          }
//
//          permissionLauncher.launch(
//               arrayOf(
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//               )
//          )
//     }
//}