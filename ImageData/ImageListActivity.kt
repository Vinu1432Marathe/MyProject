package com.vinayak.semicolon.securefolderhidefiles.ImageData

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.R

class ImageListActivity : BaseActivity() {

    private lateinit var viewModel: ImageViewModel
    private lateinit var adapter: ImageAdapter
    private lateinit var img_AllSelect: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var rlNoData: RelativeLayout
    private lateinit var folderPath: String

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.loadAllImages()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_list)

        rlNoData = findViewById(R.id.rlNoData)
        recyclerView = findViewById(R.id.rvImages)
        img_AllSelect = findViewById(R.id.img_AllSelect)
        val btnMove = findViewById<RelativeLayout>(R.id.rlHideData)

        folderPath = intent.getStringExtra("folderPath").toString()

        adapter = ImageAdapter(emptyList()) { imageModel ->
            // on selection changed
            viewModel.toggleSelection(imageModel)
        }
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this)[ImageViewModel::class.java]

        viewModel.images.observe(this) {
            if (it.isEmpty()) {

                rlNoData.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                rlNoData.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
//            loadFilesFromFolder(folderPath)
                adapter.updateList(it)
            }

        }

        btnMove.setOnClickListener {
            folderCreate()
//            viewModel.moveSelectedToHidden(folderPath)
        }

        img_AllSelect.setOnClickListener {
            viewModel.toggleSelectClearAll()
        }


        checkPermissionsAndLoad()
    }

    private fun checkPermissionsAndLoad() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.loadAllImages()
        } else {
            permissionLauncher.launch(permission)
        }
    }


    private fun folderCreate() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.dialog_image_hide)
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
        dialog.setContentView(R.layout.dialog_image_hide_securely)
        dialog.setCancelable(true)

        val tvMessage = dialog.findViewById<EditText>(R.id.tvMessage)
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
        viewModel.loadAllImages()    }

}
