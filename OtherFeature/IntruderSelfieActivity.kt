package com.vinayak.semicolon.securefolderhidefiles.OtherFeature

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.RelativeLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.R
import java.io.File

class IntruderSelfieActivity : BaseActivity() {

    private lateinit var rlNoData: RelativeLayout
    private lateinit var rvIntruderSelfie: RecyclerView
    private lateinit var adapter: IntruderPhotoAdapter
    private val photoList = mutableListOf<File>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_intruder_selfie)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvIntruderSelfie = findViewById(R.id.rvIntruderSelfie)
        rlNoData = findViewById(R.id.rlNoData)



        loadImages()
    }

    private fun loadImages() {
        val folder = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), ".HideSecureFolder/IntruderSelfie")
        if (folder.exists()) {
            val images = folder.listFiles { file ->
                file.extension.lowercase() in listOf("jpg", "jpeg", "png")
            }
            if (images != null) {
                photoList.clear()
                photoList.addAll(images.sortedByDescending { it.lastModified() })

                if (photoList.isEmpty()){
                    rlNoData.visibility = View.VISIBLE
                    rvIntruderSelfie.visibility = View.GONE

                }else{
                    rlNoData.visibility = View.GONE
                    rvIntruderSelfie.visibility = View.VISIBLE
                    adapter = IntruderPhotoAdapter(photoList)
                    rvIntruderSelfie.layoutManager = GridLayoutManager(this@IntruderSelfieActivity, 2)
                    rvIntruderSelfie.adapter = adapter
                }

            }
        }
    }
}