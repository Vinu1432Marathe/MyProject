package com.vinayak.semicolon.securefolderhidefiles.Notes

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.R
import com.vinayak.semicolon.securefolderhidefiles.Room.Note
import com.vinayak.semicolon.securefolderhidefiles.Room.NoteViewModel

class CreateNotesActivity : BaseActivity() {

    private lateinit var viewModel: NoteViewModel
    private var noteId = 0 // 0 for new note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_notes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)
        val btnSave = findViewById<TextView>(R.id.btnSave)
        val txtHeader = findViewById<TextView>(R.id.txtHeader)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(NoteViewModel::class.java)

        // Editing mode
        noteId = intent.getIntExtra("id", 0)
        if (noteId != 0) {
            etTitle.setText(intent.getStringExtra("title"))
            etContent.setText(intent.getStringExtra("content"))
            txtHeader.setText(intent.getStringExtra("Type"))
            btnSave.setText(intent.getStringExtra("Button"))
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val content = etContent.text.toString()
            if (title.isNotEmpty() && content.isNotEmpty()) {
                val note = Note(id = noteId, title = title, content = content)
                if (noteId == 0) viewModel.insert(note)
                else viewModel.update(note)
                finish()
            }
        }
    }

}