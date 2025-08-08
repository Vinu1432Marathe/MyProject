package com.vinayak.semicolon.securefolderhidefiles.Notes

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.R
import com.vinayak.semicolon.securefolderhidefiles.Room.Note
import com.vinayak.semicolon.securefolderhidefiles.Room.NoteViewModel

class NoteDetailsActivity : BaseActivity() {

    private lateinit var viewModel: NoteViewModel
    lateinit var etContent : TextView
    lateinit var etTitle : TextView
    lateinit var imgEdit : ImageView
    lateinit var imgCopy : ImageView
    lateinit var imgCopy1 : ImageView
    lateinit var llCopyAll : LinearLayout
    lateinit var llShare : LinearLayout
    lateinit var llDelete : LinearLayout
    lateinit var Title : String
    lateinit var Desc : String
    private var noteId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_note_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        llCopyAll = findViewById(R.id.llCopyAll)
        llShare = findViewById(R.id.llShare)
        llDelete = findViewById(R.id.llDelete)
        imgEdit = findViewById(R.id.imgEdit)
        imgCopy = findViewById(R.id.imgCopy)
        imgCopy1 = findViewById(R.id.imgCopy1)

         etTitle = findViewById<TextView>(R.id.etTitle)
         etContent = findViewById<TextView>(R.id.etContent)


        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )
            .get(NoteViewModel::class.java)
        noteId = intent.getIntExtra("id", -1)
        Title = intent.getStringExtra("title").toString()
        Desc = intent.getStringExtra("content").toString()

        val note = Note(id = noteId, title = Title, content = Desc)

        viewModel.notes.observe(this) { notes ->
            val note = notes.find { it.id == noteId }
            note?.let {
                etTitle.setText(it.title)
                etContent.setText(it.content)
            }
        }


        imgEdit.setOnClickListener {

            val intent = Intent(this, CreateNotesActivity::class.java)
            intent.putExtra("id", noteId)
            intent.putExtra("title", Title)
            intent.putExtra("content", Desc)
            intent.putExtra("Type", getString(R.string.edit_note))
            intent.putExtra("Button", getString(R.string.update))
            startActivity(intent)

        }

        imgCopy.setOnClickListener {

//            val fullText = "${Title}\n${Desc}"

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Note", Title)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, getString(R.string.note_copied_to_clipboard), Toast.LENGTH_SHORT).show()
        }
        imgCopy1.setOnClickListener {

//            val fullText = "${Title}\n${Desc}"

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Note", Desc)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, getString(R.string.note_copied_to_clipboard), Toast.LENGTH_SHORT).show()
        }

        llCopyAll.setOnClickListener {
            val fullText = "${Title}\n${Desc}"

            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Note", fullText)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, getString(R.string.note_copied_to_clipboard), Toast.LENGTH_SHORT).show()
        }

        llShare.setOnClickListener {

            val fullText = "${Title}\n${Desc}"
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Sharing Note")
                putExtra(Intent.EXTRA_TEXT, fullText)
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))

        }

        llDelete.setOnClickListener {
            viewModel.delete(note)
            finish()
        }


    }

}