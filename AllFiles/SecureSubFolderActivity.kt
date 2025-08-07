package com.vinayak.semicolon.securefolderhidefiles.AllFiles

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.Activity.MoveToActivity
import com.vinayak.semicolon.securefolderhidefiles.Other.AppUtils
import com.vinayak.semicolon.securefolderhidefiles.R
import java.io.File

class SecureSubFolderActivity : BaseActivity() {

    lateinit var img_AllSelect: ImageView
    lateinit var imgCreate: ImageView
    lateinit var folderPath: String
    lateinit var folderName: String
    private lateinit var rvSubFolder: RecyclerView

    private lateinit var llUnHide: LinearLayout
    private lateinit var llMove: LinearLayout
    private lateinit var llDelete: LinearLayout
    private lateinit var rlNoData: RelativeLayout

    private lateinit var viewModel: FileViewModel
    private var allSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_secure_sub_folder)
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


        rvSubFolder.layoutManager = GridLayoutManager(this, 3)

        viewModel = ViewModelProvider(this)[FileViewModel::class.java]

        viewModel.filesInFolder.observe(this) { list ->

            if (list.isEmpty()) {
                rlNoData.visibility = View.VISIBLE
                rvSubFolder.visibility = View.GONE
            } else {
                rlNoData.visibility = View.GONE
                rvSubFolder.visibility = View.VISIBLE

                rvSubFolder.adapter = SecureSubFolderAdapter(list) { audio ->
                    viewModel.toggleFileSelection(audio)

                }
            }


        }

        checkPermission()

        imgCreate.setOnClickListener {
            val intent = Intent(this, AllFilesActivity::class.java)
                .putExtra("folderPath", folderPath)
            startActivity(intent)
        }

//        img_AllSelect.setOnClickListener {
//            allSelected = !allSelected
//            if (allSelected) viewModel.selectAllAudios()
//            else viewModel.unselectAllAudios()
//            viewModel.toggleSelectClearAll()
//        }
//
        llUnHide.setOnClickListener {

            viewModel.restoreSelectedFiles(folderPath)

        }
////
        llMove.setOnClickListener {


            val selected = viewModel.filesInFolder.value?.filter { it.isSelected } ?: emptyList()
            val selectedPaths = selected.map { it.path ?: "" }

            val intent = Intent(this, MoveToActivity::class.java)
            intent.putStringArrayListExtra("selected_paths", ArrayList(selectedPaths))
                .putExtra("folderName", folderName)
            startActivity(intent)
//                val intent = Intent(this, MoveToActivity::class.java)
//                    .putExtra("folderName", folderName)
//                startActivity(intent)


        }
//
        llDelete.setOnClickListener {
//            viewModel.deleteSelected(folderPath)
            val downloadDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val nestedFolder = File(downloadDir, AppUtils.Main_Folder + "/FolderBin")

            viewModel.moveSelectedFiles11(nestedFolder.toString())

        }
        viewModel.loadFilesFromFolderPath(folderPath)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                viewModel.loadFilesFromFolderPath(folderPath)
            } else {
                startActivity(
                    Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                        .setData(Uri.parse("package:$packageName"))
                )
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                123
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFilesFromFolderPath(folderPath)

    }

    override fun onRequestPermissionsResult(req: Int, perms: Array<out String>, res: IntArray) {
        super.onRequestPermissionsResult(req, perms, res)
        viewModel.loadFilesFromFolderPath(folderPath)
    }
}

