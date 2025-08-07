package com.vinayak.semicolon.securefolderhidefiles.VideoData

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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
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

class VideoSubFolderActivity : BaseActivity() {

    lateinit var img_AllSelect: ImageView
    lateinit var imgCreate: ImageView
    lateinit var folderPath: String
    lateinit var folderName: String
    private lateinit var rvSubFolder: RecyclerView

    private lateinit var llUnHide: LinearLayout
    private lateinit var llMove: LinearLayout
    private lateinit var llDelete: LinearLayout
    private lateinit var rlNoData: RelativeLayout

    private lateinit var viewModel: VideoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_video_sub_folder)
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


        rvSubFolder.layoutManager = GridLayoutManager(this,2)

        viewModel = ViewModelProvider(this)[VideoViewModel::class.java]

        viewModel.videos.observe(this) { list ->

            if (list.isEmpty()){
                rlNoData.visibility = View.VISIBLE
                rvSubFolder.visibility = View.GONE
            }else{
                rlNoData.visibility = View.GONE
                rvSubFolder.visibility = View.VISIBLE

                rvSubFolder.adapter = VideoSubFolderAdapter(list){audio ->
                    viewModel.toggleSelection(audio)

                }
            }

        }
        img_AllSelect.setOnClickListener {
            viewModel. toggleSelectClearAll()

        }
        checkPermission()

        imgCreate.setOnClickListener {
            val intent = Intent(this, VideoListActivity::class.java)
                .putExtra("folderPath", folderPath)
            startActivity(intent)
        }

        img_AllSelect.setOnClickListener {
            viewModel.toggleSelectClearAll()
        }
//
        llUnHide.setOnClickListener {
            if (viewModel.selectedItems.isEmpty()){
                Toast.makeText(this, "Select Audio first!!", Toast.LENGTH_SHORT).show()
            }else{

                viewModel.restoreSelectedVideos(folderPath)
            }

        }
//
        llMove.setOnClickListener {
            if (viewModel.selectedItems.isEmpty()){
                Toast.makeText(this, "Select Audio first!!", Toast.LENGTH_SHORT).show()
            }else{

                val selected = viewModel.videos.value?.filter { it.isSelected } ?: emptyList()
                val selectedPaths = selected.map { it.uri.path ?: "" }

                val intent = Intent(this, MoveToActivity::class.java)
                intent.putStringArrayListExtra("selected_paths", ArrayList(selectedPaths))
                    .putExtra("folderName",folderName)
                    .putExtra("folderPath",folderPath)
                startActivity(intent)

            }

        }
//
        llDelete.setOnClickListener {
//            viewModel.deleteSelected(folderPath)
//            viewModel.moveSelectedVideosToFolder(selectedVideo, newfolderPath)
            val downloadDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val nestedFolder = File(downloadDir, AppUtils.Main_Folder+"/FolderBin")

            viewModel.moveSelectedToDelete( nestedFolder.toString())


        }

    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                viewModel.loadHiddenVideos(folderPath)
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
            viewModel.loadHiddenVideos(folderPath)

    }

    override fun onRequestPermissionsResult(req: Int, perms: Array<out String>, res: IntArray) {
        super.onRequestPermissionsResult(req, perms, res)
        viewModel.loadHiddenVideos(folderPath)
    }
}