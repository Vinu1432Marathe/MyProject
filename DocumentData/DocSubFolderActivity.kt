package com.vinayak.semicolon.securefolderhidefiles.DocumentData

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.Activity.MoveToActivity
import com.vinayak.semicolon.securefolderhidefiles.Other.AppUtils
import com.vinayak.semicolon.securefolderhidefiles.R
import java.io.File

class DocSubFolderActivity : BaseActivity() {

    lateinit var img_AllSelect: ImageView
    lateinit var imgCreate: ImageView
    lateinit var folderPath: String
    lateinit var folderName: String
    private lateinit var adapter: DocSubFolderAdapter
    private lateinit var rvSubFolder: RecyclerView

    private lateinit var llUnHide: LinearLayout
    private lateinit var llMove: LinearLayout
    private lateinit var llDelete: LinearLayout
    private lateinit var rlNoData: RelativeLayout

    private lateinit var viewModel: DocumentViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_sub_folder)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        folderName = intent.getStringExtra("folderName").toString()
        folderPath = intent.getStringExtra("folderPath").toString()
        Log.e("folderPath", "folderPath :: $folderPath")

        img_AllSelect = findViewById(R.id.img_AllSelect)
        imgCreate = findViewById(R.id.imgCreate)
        rvSubFolder = findViewById(R.id.rvSubFolder)
        llUnHide = findViewById(R.id.llUnHide)
        llMove = findViewById(R.id.llMove)
        llDelete = findViewById(R.id.llDelete)
        rlNoData = findViewById(R.id.rlNoData)


        viewModel = ViewModelProvider(this)[DocumentViewModel::class.java]


        adapter = DocSubFolderAdapter(mutableListOf()) { pos ->

            viewModel.toggleSelection(pos)
        }
        rvSubFolder.layoutManager = LinearLayoutManager(this)
        rvSubFolder.adapter = adapter

        viewModel.documents.observe(this) { list ->

            if (list.isEmpty()) {
                rlNoData.visibility = View.VISIBLE
                rvSubFolder.visibility = View.GONE
            } else {
                rlNoData.visibility = View.GONE
                rvSubFolder.visibility = View.VISIBLE

                adapter.updateData(list)
            }

        }

//        checkPermission()

        imgCreate.setOnClickListener {
            val intent = Intent(this, DocListActivity::class.java)
                .putExtra("folderPath", folderPath)
            startActivity(intent)
        }

        img_AllSelect.setOnClickListener {
            viewModel.toggleSelectAll()
        }

        llUnHide.setOnClickListener {

            viewModel.restoreSelected(folderPath)

        }
//
        llMove.setOnClickListener {


            val selected = viewModel.documents.value?.filter { it.isSelected } ?: emptyList()
            val selectedPaths = selected.map { it.uri.path ?: "" }

            val intent = Intent(this, MoveToActivity::class.java)
            intent.putStringArrayListExtra("selected_paths", ArrayList(selectedPaths))
                .putExtra("folderName", folderName)
            startActivity(intent)

        }
//
        llDelete.setOnClickListener {

            val downloadDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val nestedFolder = File(downloadDir, AppUtils.Main_Folder + "/FolderBin")

            viewModel.moveSelectedDocuments11(nestedFolder.toString())

        }

        viewModel.loadDocumentsFromPath(folderPath)

    }


    override fun onResume() {
        super.onResume()
        viewModel.loadDocumentsFromPath(folderPath)

    }

}
