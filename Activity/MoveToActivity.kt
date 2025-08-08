package com.vinayak.semicolon.securefolderhidefiles.Activity

import android.app.ProgressDialog.show
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vinayak.semicolon.securefolderhidefiles.AllFiles.FileViewModel
import com.vinayak.semicolon.securefolderhidefiles.AudioData.AudioViewModel
import com.vinayak.semicolon.securefolderhidefiles.DocumentData.DocumentViewModel
import com.vinayak.semicolon.securefolderhidefiles.ImageData.ImageViewModel
import com.vinayak.semicolon.securefolderhidefiles.Model.AudioModel
import com.vinayak.semicolon.securefolderhidefiles.Model.DocumentModel
import com.vinayak.semicolon.securefolderhidefiles.Model.FileModel
import com.vinayak.semicolon.securefolderhidefiles.Model.FolderItem
import com.vinayak.semicolon.securefolderhidefiles.Model.ImageModel
import com.vinayak.semicolon.securefolderhidefiles.Model.VideoModel
import com.vinayak.semicolon.securefolderhidefiles.Other.AppUtils
import com.vinayak.semicolon.securefolderhidefiles.R
import com.vinayak.semicolon.securefolderhidefiles.VideoData.VideoViewModel
import java.io.File

class MoveToActivity : BaseActivity() {

    private lateinit var newfolderPath: String
    private lateinit var currentfolderPath: String
    private lateinit var folderName: String
    lateinit var rlFolderCreate: RelativeLayout
    lateinit var rlNoData: RelativeLayout
    lateinit var rvFolder: RecyclerView
    lateinit var btnMove: TextView

    private lateinit var viewModelDocument: DocumentViewModel

    private lateinit var viewModelVideo: VideoViewModel
    private lateinit var viewModelImage: ImageViewModel
    private lateinit var viewModelAudio: AudioViewModel
    private lateinit var viewModelFile: FileViewModel
    lateinit var moveDataFolderAdapter: MoveDataFolderAdapter
    private val folderList = mutableListOf<FolderItem>()

    private lateinit var selectedDocuments: List<DocumentModel>
    private lateinit var selectedVideo: List<VideoModel>
    private lateinit var selectedImage: List<ImageModel>
    private lateinit var selectedAudio: List<AudioModel>
    private lateinit var selectedFile: List<FileModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_move_to)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rlFolderCreate = findViewById(R.id.rlFolderCreate)
        btnMove = findViewById(R.id.btnMove)
        rvFolder = findViewById(R.id.rvFolder)
        rlNoData = findViewById(R.id.rlNoData)


        currentfolderPath = intent.getStringExtra("folderPath").toString()
        folderName = intent.getStringExtra("folderName").toString()


        Log.e("folderPath", "folderPath Move To:: $folderName")




        moveDataFolderAdapter = MoveDataFolderAdapter(this, emptyList()) { path ->
            newfolderPath = path

        }
        rvFolder.layoutManager = LinearLayoutManager(this)
        rvFolder.adapter = moveDataFolderAdapter

        when {
            folderName.equals("FolderAudio") -> {
                getAudio()
            }

            folderName.equals("FolderVideo") -> {
                getVideo()
            }

            folderName.equals("FolderImage") -> {
                getImage()
            }

            folderName.equals("FolderDocument") -> {
                getDocument()
            }

            folderName.equals("ALLFile") -> {
                getAllFolder()
            }
        }





        rlFolderCreate.setOnClickListener {
            folderCreate()
        }

        btnMove.setOnClickListener {

            when {
                folderName.equals("FolderAudio") -> {
                    viewModelAudio.moveSelectedAudiosToFolder(newfolderPath)
                }

                folderName.equals("FolderVideo") -> {
                    viewModelVideo.moveSelectedVideosToFolder(selectedVideo, newfolderPath)
                }

                folderName.equals("FolderImage") -> {
                    viewModelImage.moveDocumentsBetweenFolders(selectedImage, newfolderPath)
                }

                folderName.equals("FolderDocument") -> {
                    viewModelDocument.moveDocumentsBetweenFolders(selectedDocuments, newfolderPath)
                }

                folderName.equals("ALLFile") -> {
                    viewModelFile.moveSelectedFiles11(selectedFile, newfolderPath)
                }
            }

            finish()
        }

    }

    private fun folderCreate() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.dialog_create_folder)
        dialog.setCancelable(true)

        val tvMessage = dialog.findViewById<EditText>(R.id.tvMessage)
        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        val btnCreate = dialog.findViewById<TextView>(R.id.btnExit)

        btnCreate?.setOnClickListener {
            val newName = tvMessage?.text.toString().trim()
            if (newName.isNotEmpty()) {
                createNestedFolder(AppUtils.Main_Folder, folderName, newName)
            }
            dialog.dismiss()
            loadFolders()
        }

        btnCancel?.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }


    private fun createNestedFolder(mainFolder: String, subFolder: String, newFolder: String) {
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val nestedFolder = File(downloadDir, "$mainFolder/$subFolder/$newFolder")

        if (!nestedFolder.exists()) {
            if (nestedFolder.mkdirs()) {
//                Toast.makeText(this, "Created: ${nestedFolder.name} folder", Toast.LENGTH_SHORT)
//                    .show()
            } else {
                Toast.makeText(this, "Failed to create folder", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadFolders() {
        folderList.clear()

        val baseDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "${AppUtils.Main_Folder}/$folderName"
        )

        if (baseDir.exists() && baseDir.isDirectory) {
            baseDir.listFiles()?.filter { it.isDirectory }?.forEach { folder ->
                val count = folder.listFiles()?.count { it.isFile } ?: 0
                folderList.add(FolderItem(folder.name, folder.absolutePath, count))
            }
        }

        moveDataFolderAdapter.updateList(folderList)

        rlNoData.visibility = if (folderList.isEmpty()) View.VISIBLE else View.GONE
        rvFolder.visibility = if (folderList.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun getDocument() {
        viewModelDocument = ViewModelProvider(this)[DocumentViewModel::class.java]
        val selectedPaths = intent.getStringArrayListExtra("selected_paths") ?: arrayListOf()
        selectedDocuments = selectedPaths.mapNotNull { path ->
            val file = File(path)
            if (file.exists()) {
                DocumentModel(
                    uri = Uri.fromFile(file),
                    name = file.name,
                    size = file.length(),
                    isSelected = true
                )
            } else null
        }

    }

    private fun getImage() {
        viewModelImage = ViewModelProvider(this)[ImageViewModel::class.java]
        val selectedPaths = intent.getStringArrayListExtra("selected_paths") ?: arrayListOf()
        selectedImage = selectedPaths.mapNotNull { path ->
            val file = File(path)
            if (file.exists()) {
                ImageModel(
                    uri = Uri.fromFile(file),
                    name = file.name,
                    size = file.length(),
                    isSelected = true
                )
            } else null
        }

        viewModelImage.selectedItems.clear()
        viewModelImage.selectedItems.addAll(selectedImage)

    }

    private fun getVideo() {
        viewModelVideo = ViewModelProvider(this)[VideoViewModel::class.java]
        val selectedPaths = intent.getStringArrayListExtra("selected_paths") ?: arrayListOf()
        selectedVideo = selectedPaths.mapNotNull { path ->
            val file = File(path)
            if (file.exists()) {
                VideoModel(
                    uri = Uri.fromFile(file),
                    name = file.name,
                    size = file.length(),
                    isSelected = true
                )
            } else null
        }

        viewModelVideo.selectedItems.clear()
        viewModelVideo.selectedItems.addAll(selectedVideo)

    }

    private fun getAudio() {
        viewModelAudio = ViewModelProvider(this)[AudioViewModel::class.java]
        val selectedPaths = intent.getStringArrayListExtra("selected_paths") ?: arrayListOf()
        selectedAudio = selectedPaths.mapIndexed { index, path ->
            AudioModel(
                id = index.toLong(),
                title = File(path).nameWithoutExtension,
                artist = "Unknown",
                duration = 0L,
                data = path
            )
        }


        viewModelAudio.selectedItems.clear()
        viewModelAudio.selectedItems.addAll(selectedAudio)

    }

    private fun getAllFolder() {
        viewModelFile = ViewModelProvider(this)[FileViewModel::class.java]
        val selectedPaths = intent.getStringArrayListExtra("selected_paths") ?: arrayListOf()
        selectedFile = selectedPaths.map { path ->
            val file = File(path)
            FileModel(
                name = file.name,
                path = file.absolutePath,
                size = file.length(),
                isDirectory = file.isDirectory,
                isSelected = true,
                originalPath = file.absolutePath // optional
            )
        }

//        viewModelFile.selectedFiles.clear()
//        viewModelFile.selectedFiles.addAll(selectedFile)

    }

    override fun onResume() {
        super.onResume()
        loadFolders()
    }
}