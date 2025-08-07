package com.vinayak.semicolon.securefolderhidefiles.VideoData

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.R

class VideoListActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VideoAdapter
    private lateinit var viewModel: VideoViewModel
    private lateinit var img_AllSelect: ImageView
    private lateinit var rlHideData: RelativeLayout
    private lateinit var rlNoData: RelativeLayout
    private lateinit var folderPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_video_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


            rlNoData = findViewById(R.id.rlNoData)
        rlHideData = findViewById(R.id.rlHideData)
        img_AllSelect = findViewById(R.id.img_AllSelect)
        recyclerView = findViewById(R.id.recyclerView)
        viewModel = ViewModelProvider(this)[VideoViewModel::class.java]

        folderPath = intent.getStringExtra("folderPath").toString()

        Log.e("chekkkkFolderrr", "Folder :: $folderPath")

        viewModel.videos.observe(this) { videos ->

            if (videos.isEmpty()) {

                rlNoData.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                rlNoData.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
//            loadFilesFromFolder(folderPath)
                adapter = VideoAdapter(videos) { videoModel ->
                    viewModel.toggleSelection(videoModel)
                }
                val layoutManager = GridLayoutManager(this, 2)
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = adapter
            }



        }
        img_AllSelect.setOnClickListener {
            viewModel.toggleSelectClearAll()

        }

        rlHideData.setOnClickListener {
            folderCreate()
//            viewModel.moveSelectedToHidden(folderPath)
        }

        checkPermission()
    }

    private fun checkPermission() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_VIDEO
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 101)
        } else {
            viewModel.loadAllVideos()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == 101 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.loadAllVideos()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun folderCreate() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.dialog_video_hide)
        dialog.setCancelable(true)

        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        val btnCreate = dialog.findViewById<TextView>(R.id.btnExit)

        btnCreate?.setOnClickListener {

            ImageHiddenSecurely()

//                createNestedFolder(AppUtils.Main_Folder, folderName, newName)

            dialog.dismiss()
        }

        btnCancel?.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun ImageHiddenSecurely() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.dialog_video_hide_securely)
        dialog.setCancelable(true)

        val btnCancel = dialog.findViewById<TextView>(R.id.btnCancel)
        val btnCreate = dialog.findViewById<TextView>(R.id.btnExit)

        btnCreate?.setOnClickListener {


            viewModel.moveSelectedToHidden(folderPath)
//                createNestedFolder(AppUtils.Main_Folder, folderName, newName)

            dialog.dismiss()
        }

        btnCancel?.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadAllVideos()
    }
}