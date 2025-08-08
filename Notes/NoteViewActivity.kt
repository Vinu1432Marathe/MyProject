package com.vinayak.semicolon.securefolderhidefiles.Notes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.R
import com.vinayak.semicolon.securefolderhidefiles.Room.NoteViewModel

class NoteViewActivity : BaseActivity() {

    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var rlNoData: RelativeLayout
    private lateinit var rvNotes: RecyclerView
    private lateinit var flData: FrameLayout
    private lateinit var btnAdd: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_note_view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        btnAdd = findViewById<ImageView>(R.id.imgCreate)
        rvNotes = findViewById<RecyclerView>(R.id.rvNotes)
        rlNoData = findViewById(R.id.rlNoData)

        viewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        adapter = NoteAdapter(this,

            onEditClick = {
                val intent = Intent(this, CreateNotesActivity::class.java)
                intent.putExtra("id", it.id)
                intent.putExtra("title", it.title)
                intent.putExtra("content", it.content)
                intent.putExtra("Type", getString(R.string.edit_note))
                intent.putExtra("Button", getString(R.string.update))
                startActivity(intent)
            },
            onDeleteClick = {
                viewModel.delete(it)
            },
            onClick = {
                val intent = Intent(this, NoteDetailsActivity::class.java)
                intent.putExtra("id", it.id)
                intent.putExtra("title", it.title)
                intent.putExtra("content", it.content)
                startActivity(intent)
            }

        )

        rvNotes.layoutManager = LinearLayoutManager(this)
        rvNotes.adapter = adapter


        viewModel.notes.observe(this) {
            Log.e("listData", "Lsit :: ${it.size}")

            if (it.isEmpty()) {
                rlNoData.visibility = View.VISIBLE
                rvNotes.visibility = View.GONE
            } else {
                rlNoData.visibility = View.GONE
                rvNotes.visibility = View.VISIBLE
                adapter.setData(it)
            }
        }

        btnAdd.setOnClickListener {
            startActivity(Intent(this, CreateNotesActivity::class.java))
        }
    }
}