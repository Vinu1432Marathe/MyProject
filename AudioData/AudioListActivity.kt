package com.vinayak.semicolon.securefolderhidefiles.AudioData

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.R

class AudioListActivity : BaseActivity() {


    private lateinit var adapter: AudioAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var rlHideData: RelativeLayout
    private lateinit var rlNoData: RelativeLayout
    private lateinit var img_AllSelect: ImageView
    private lateinit var folderPath: String
    private val viewModel: AudioViewModel by viewModels()
    private var allSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_audio_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        img_AllSelect = findViewById(R.id.img_AllSelect)
        rlHideData = findViewById(R.id.rlHideData)
        rlNoData = findViewById(R.id.rlNoData)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        folderPath = intent.getStringExtra("folderPath").toString()
        Log.e("checkkPath", "FilePath  :: $folderPath")


        adapter = AudioAdapter(emptyList()) {
            rlHideData.isEnabled = adapter.getSelectedItems().isNotEmpty()


        }

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        rlHideData.setOnClickListener {
            viewModel.moveSelectedAudiosToHiddenFolder(folderPath)
            Toast.makeText(this, "Files moved!", Toast.LENGTH_SHORT).show()
        }

        viewModel.audioList.observe(this, Observer {
            if (it.isEmpty()) {

                rlNoData.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                rlNoData.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                adapter.updateList(it)
            }

        })


        img_AllSelect.setOnClickListener {
            allSelected = !allSelected
            if (allSelected) viewModel.selectAllAudios()
            else viewModel.unselectAllAudios()
        }
        requestPermissions()
    }

    private fun requestPermissions() {
        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val granted = permissions.entries.all { it.value == true }
            if (granted) {
                viewModel.loadAudioFiles()
            } else {
                Toast.makeText(this, "Storage permission is required", Toast.LENGTH_LONG).show()
            }
        }

        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }
}