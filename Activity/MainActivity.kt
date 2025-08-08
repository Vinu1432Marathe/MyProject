package com.vinayak.semicolon.securefolderhidefiles.Activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vinayak.semicolon.securefolderhidefiles.AllFiles.SecureFolderActivity
import com.vinayak.semicolon.securefolderhidefiles.AudioData.AudioFolderActivity
import com.vinayak.semicolon.securefolderhidefiles.ContactData.HideContactActivity
import com.vinayak.semicolon.securefolderhidefiles.DocumentData.DocFolderActivity
import com.vinayak.semicolon.securefolderhidefiles.ImageData.IMGFolderActivity
import com.vinayak.semicolon.securefolderhidefiles.Notes.NoteViewActivity
import com.vinayak.semicolon.securefolderhidefiles.Other.AppUtils
import com.vinayak.semicolon.securefolderhidefiles.OtherFeature.DisguiseModeActivity
import com.vinayak.semicolon.securefolderhidefiles.OtherFeature.IntruderSelfieActivity
import com.vinayak.semicolon.securefolderhidefiles.OtherFeature.RecycleBinActivity
import com.vinayak.semicolon.securefolderhidefiles.R
import com.vinayak.semicolon.securefolderhidefiles.VideoData.VideoFolderActivity
import java.io.File

class MainActivity : BaseActivity() {

    lateinit var img_Menu: ImageView
    lateinit var llAllFiles: LinearLayout
    lateinit var llImage: LinearLayout
    lateinit var llVideo: LinearLayout
    lateinit var llAudio: LinearLayout
    lateinit var llDocument: LinearLayout
    lateinit var llContact: LinearLayout
    lateinit var llSecretNote: LinearLayout
    lateinit var llRecycleBin: LinearLayout
    lateinit var llDisguisedMode: LinearLayout
    lateinit var llIntruderSelfie: LinearLayout

    private var pathFolder: String? = null
    private var subFolder: String = "FolderBin"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        img_Menu = findViewById(R.id.img_Menu)
        llAllFiles = findViewById(R.id.llAllFiles)
        llImage = findViewById(R.id.llImage)
        llVideo = findViewById(R.id.llVideo)
        llAudio = findViewById(R.id.llAudio)
        llDocument = findViewById(R.id.llDocument)
        llContact = findViewById(R.id.llContact)
        llSecretNote = findViewById(R.id.llSecretNote)
        llRecycleBin = findViewById(R.id.llRecycleBin)
        llDisguisedMode = findViewById(R.id.llDisguisedMode)
        llIntruderSelfie = findViewById(R.id.llIntruderSelfie)

        createHiddenSubfolder(AppUtils.Main_Folder, subFolder)


        img_Menu.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)

            startActivity(intent)
        }

        llAllFiles.setOnClickListener {

            subFolder = "ALLFile"

            createHiddenSubfolder(AppUtils.Main_Folder, subFolder)
//
            val intent = Intent(this, SecureFolderActivity::class.java)
                .putExtra("FolderName", subFolder)
            startActivity(intent)

        }

        llImage.setOnClickListener {
            subFolder = "FolderImage"
            createHiddenSubfolder(AppUtils.Main_Folder, subFolder)
            val intent = Intent(this, IMGFolderActivity::class.java)
                .putExtra("FolderName", subFolder)
            startActivity(intent)

        }

        llVideo.setOnClickListener {

            subFolder = "FolderVideo"

            createHiddenSubfolder(AppUtils.Main_Folder, subFolder)
            val intent = Intent(this, VideoFolderActivity::class.java)
                .putExtra("FolderName", subFolder)
            startActivity(intent)


        }

        llAudio.setOnClickListener {
            subFolder = "FolderAudio"

            createHiddenSubfolder(AppUtils.Main_Folder, subFolder)
            val intent = Intent(this, AudioFolderActivity::class.java)
                .putExtra("FolderName", subFolder)
            startActivity(intent)

        }

        llDocument.setOnClickListener {
            subFolder = "FolderDocument"
            createHiddenSubfolder(AppUtils.Main_Folder, subFolder)
            val intent = Intent(this, DocFolderActivity::class.java)
                .putExtra("FolderName", subFolder)
            startActivity(intent)


        }

        llContact.setOnClickListener {
            val intent = Intent(this, HideContactActivity::class.java)
//                .putExtra("FolderName", subFolder)
            startActivity(intent)

        }

        llSecretNote.setOnClickListener {
            val intent = Intent(this, NoteViewActivity::class.java)
            startActivity(intent)
        }

        llRecycleBin.setOnClickListener {
            subFolder = "FolderBin"
            createHiddenSubfolder(AppUtils.Main_Folder, subFolder)
            val intent = Intent(this, RecycleBinActivity::class.java)
                .putExtra("FolderName", pathFolder)
            startActivity(intent)

        }

        llDisguisedMode.setOnClickListener {

            val intent = Intent(this, DisguiseModeActivity::class.java)
            startActivity(intent)
        }

        llIntruderSelfie.setOnClickListener {
            subFolder = "IntruderSelfie"

            createHiddenSubfolder(AppUtils.Main_Folder, subFolder)
            val intent = Intent(this, IntruderSelfieActivity::class.java)
                .putExtra("FolderName", subFolder)
            startActivity(intent)


        }

    }

    private fun createHiddenSubfolder(hiddenFolderName: String, subFolderName: String) {
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val hiddenFolder = File(downloadDir, hiddenFolderName)
        val subFolder = File(hiddenFolder, subFolderName)

        if (!subFolder.exists()) {
            val created = subFolder.mkdirs()
            if (created) {
                pathFolder = subFolder.absolutePath
                Log.e("CheckPath", "SubFile created:\n${subFolder.absolutePath}")
                Toast.makeText(this, "Created: ${subFolder.absolutePath}", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Failed to create subfolder", Toast.LENGTH_SHORT).show()
            }
        } else {
            pathFolder = subFolder.absolutePath
            Log.e("CheckPath", "SubFile created:\n${subFolder.absolutePath}")
            Toast.makeText(this, "Subfolder already exists", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        showCongratulationsDialog()
    }
    private fun showCongratulationsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_exit)
        dialog.setCancelable(false)

        val mbtn_no = dialog.findViewById<TextView>(R.id.btnCancel)
        val mbtn_yes = dialog.findViewById<TextView>(R.id.btnExit)

        mbtn_yes.setOnClickListener { finishAffinity() }

        mbtn_no.setOnClickListener { dialog.dismiss() }
        val window = dialog.window
        val wlp = window!!.attributes

        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dialog.show()

    }
}