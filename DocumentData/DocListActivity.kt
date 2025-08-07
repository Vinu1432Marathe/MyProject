package com.vinayak.semicolon.securefolderhidefiles.DocumentData

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.R

class DocListActivity : BaseActivity() {

    private lateinit var viewModel: DocumentViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var rlHideData: RelativeLayout
    private lateinit var rlNoData: RelativeLayout
    private lateinit var adapter: DocumentAdapter
    private lateinit var folderPath: String
    private lateinit var img_AllSelect: ImageView
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.loadDocuments()
        else Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_doc_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        folderPath = intent.getStringExtra("folderPath").toString()
        rlHideData = findViewById(R.id.rlHideData)
        recyclerView = findViewById(R.id.recyclerView)
        img_AllSelect = findViewById(R.id.img_AllSelect)
        rlNoData = findViewById(R.id.rlNoData)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        adapter = DocumentAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter



        viewModel = ViewModelProvider(this)[DocumentViewModel::class.java]
        viewModel.documents.observe(this) {

            if (it.isEmpty()) {

                rlNoData.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                rlNoData.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
//            loadFilesFromFolder(folderPath)
                adapter.updateData(it)
            }

        }

        img_AllSelect.setOnClickListener {
            viewModel.toggleSelectAll()
        }

        rlHideData.setOnClickListener {

            viewModel.moveSelectedDocuments(folderPath)
        }

        viewModel.loadDocuments()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDocuments()
    }
}