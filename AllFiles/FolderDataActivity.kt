package com.vinayak.semicolon.securefolderhidefiles.AllFiles

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.Model.FileModel
import com.vinayak.semicolon.securefolderhidefiles.R
import java.io.File

class FolderDataActivity : BaseActivity() {

//    lateinit var recyclerView: RecyclerView
//    lateinit var rlNoData: RelativeLayout
//    lateinit var rlHideData: RelativeLayout
//    private val filesList = mutableListOf<File>()

    private lateinit var folderPath: String
    private lateinit var foldPath: String
    private lateinit var txtHeader: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var rlNoData: RelativeLayout
    private lateinit var rlHideData: RelativeLayout
    private lateinit var adapter: FileAdapter
    private lateinit var viewModel: FileViewModel
    private val files = mutableListOf<FileModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_folder_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[FileViewModel::class.java]

        rlHideData = findViewById(R.id.rlHideData)
        rlNoData = findViewById(R.id.rlNoData)
        txtHeader = findViewById(R.id.txtHeader)
        recyclerView = findViewById(R.id.rvFolder)
        recyclerView.layoutManager = GridLayoutManager(this, 2)


        adapter = FileAdapter(files) { filesData ->
            viewModel.toggleFileSelection(filesData)
        }
        recyclerView.adapter = adapter

        folderPath = intent.getStringExtra("folderPath") ?: return
        val folderName = intent.getStringExtra("folderName") ?: return
        foldPath = intent.getStringExtra("foldPath").toString()
        Log.e("checkkkFolder", "Folder11 :: $foldPath")


        txtHeader.text = folderName




        rlHideData.setOnClickListener {

            viewModel.moveSelectedFiles(foldPath)
            finish()


        }
//
//
//        recyclerView.adapter = FolderAdapter(filesList)
    }

    override fun onResume() {
        super.onResume()
        folderPath = intent.getStringExtra("folderPath") ?: return
        loadFilesFromFolder(folderPath)
    }
    private fun loadFilesFromFolder(path: String) {
        val folder = File(path)
        val list = folder.listFiles()?.map {
            FileModel(
                name = it.name,
                path = it.absolutePath,
                size = it.length(),
                isDirectory = it.isDirectory
            )
        } ?: emptyList()

        files.clear()
        files.addAll(list.filter { !it.isDirectory })


        if (files.isEmpty()) {

            rlNoData.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            rlNoData.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
//            loadFilesFromFolder(folderPath)
            adapter.notifyDataSetChanged()
        }
    }




}