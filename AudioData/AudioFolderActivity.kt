package com.vinayak.semicolon.securefolderhidefiles.AudioData

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.AllFiles.SecureFolderAdapter
import com.vinayak.semicolon.securefolderhidefiles.Model.FolderItem
import com.vinayak.semicolon.securefolderhidefiles.Other.AppUtils
import com.vinayak.semicolon.securefolderhidefiles.R
import java.io.File

class AudioFolderActivity : BaseActivity() {

    private lateinit var rlNoData: RelativeLayout
    private lateinit var imgCreate: ImageView
    private lateinit var rvFolder: RecyclerView
    private lateinit var folderAdapter: SecureFolderAdapter
    private val folderList = mutableListOf<FolderItem>()
    private lateinit var folderName: String
    private lateinit var folderPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_audio_folder)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        imgCreate = findViewById(R.id.imgCreate)
        rvFolder = findViewById(R.id.rvFolder)
        rlNoData = findViewById(R.id.rlNoData)

        folderName = intent.getStringExtra("FolderName") ?: "FolderAudio"
        folderPath = intent.getStringExtra("FolderName") ?: "FolderAudio"
        Log.e("folderPath", "folderPath :: $folderPath")

        folderAdapter = SecureFolderAdapter(this,folderList,folderName,folderPath)
        rvFolder.layoutManager = LinearLayoutManager(this)
        rvFolder.adapter = folderAdapter



        imgCreate.setOnClickListener {
            folderCreate()
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
        }

        btnCancel?.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun createNestedFolder(mainFolder: String, subFolder: String, newFolder: String) {
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val nestedFolder = File(downloadDir, "$mainFolder/$subFolder/$newFolder")

        if (!nestedFolder.exists()) {
            if (nestedFolder.mkdirs()) {
                folderPath = nestedFolder.absolutePath

                Log.e("checkkkPath","Path :: ${nestedFolder.absolutePath}")
                Toast.makeText(this, "Created: ${nestedFolder.absolutePath}", Toast.LENGTH_SHORT).show()
                loadFolders()
            } else {
                folderPath = nestedFolder.absolutePath

                Toast.makeText(this, "Failed to create folder", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Folder already exists", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun loadFolders() {
//        folderList.clear()
//        val baseDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
//            AppUtils.Main_Folder+"/"+folderName)
//
//        if (baseDir.exists() && baseDir.isDirectory) {
//            baseDir.listFiles()?.filter { it.isDirectory }?.forEach {
//                Log.e("checkkkPath","Path 22:: ${it.name}")
//                Log.e("checkkkPath","Path 33:: ${it.absolutePath}")
//                folderList.add(FolderItem(it.name, it.absolutePath))
//            }
//        }
//
//        folderAdapter.updateList(folderList)
//
//        if (folderList.isEmpty()) {
//            rvFolder.visibility = View.GONE
//            rlNoData.visibility = View.VISIBLE
//        } else {
//            rvFolder.visibility = View.VISIBLE
//            rlNoData.visibility = View.GONE
//        }
//    }

    private fun loadFolders() {
        folderList.clear()

        val baseDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            AppUtils.Main_Folder + "/" + folderName
        )

        if (baseDir.exists() && baseDir.isDirectory) {
            baseDir.listFiles()?.filter { it.isDirectory }?.forEach { folder ->
                val files = folder.listFiles()
                val count = files?.count { it.isFile } ?: 0

                Log.e("checkkkPath", "Path 22:: ${folder.name}")
                Log.e("checkkkPath", "Path 33:: ${folder.absolutePath}")
                Log.e("checkkkPath", "File Count: $count")

                folderList.add(FolderItem(folder.name, folder.absolutePath, count))
            }
        }

        folderAdapter.updateList(folderList)

        if (folderList.isEmpty()) {
            rvFolder.visibility = View.GONE
            rlNoData.visibility = View.VISIBLE
        } else {
            rvFolder.visibility = View.VISIBLE
            rlNoData.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        loadFolders()
    }
}