package com.vinayak.semicolon.securefolderhidefiles.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import android.Manifest
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.vinayak.semicolon.securefolderhidefiles.LockScreen.LockSceenActivity
import com.vinayak.semicolon.securefolderhidefiles.LockScreen.MainLockSceenActivity
import com.vinayak.semicolon.securefolderhidefiles.Other.AppUtils
import com.vinayak.semicolon.securefolderhidefiles.Other.SharePref
import com.vinayak.semicolon.securefolderhidefiles.R
import kotlin.jvm.java

class PermissionActivity : BaseActivity() {

    lateinit var btnEnable: TextView
    private val PERMISSION_REQUEST_CODE = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_permission)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val SavedPin = SharePref.getPin(this)
        btnEnable = findViewById(R.id.btnEnable)

        btnEnable.setOnClickListener {

            Log.e("cjeckkkkk","Pass :: $SavedPin")
            if (checkPermission()) {
                createHiddenFolderInDownloads(AppUtils.Main_Folder)
                if (!SavedPin.equals(null)){
//                    val intent = Intent(this, MainLockSceenActivity::class.java)
                    val intent = Intent(this, MainLockSceenActivity::class.java)
                    startActivity(intent)
                }else{
                    val intent = Intent(this, LockSceenActivity::class.java)
                        .putExtra("FileName", "Audio")
                    intent.putExtra("ComeFrom","Permission")
                    startActivity(intent)
                }

            } else {
                requestPermission()
            }
        }

    }


    private fun createHiddenFolderInDownloads(folderName: String) {
        val downloadsDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val hiddenFolder = File(downloadsDir, folderName)

        if (!hiddenFolder.exists()) {
            val created = hiddenFolder.mkdirs()
            if (created) {

                Log.e("CheckPath","File created:\n${hiddenFolder.absolutePath}")
                Toast.makeText(
                    this,
                    "Hidden folder created:\n${hiddenFolder.absolutePath}",
                    Toast.LENGTH_LONG
                ).show()
            } else {

                Toast.makeText(this, "Failed to create folder", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("CheckPath","File created:\n${hiddenFolder.absolutePath}")
            Toast.makeText(this, "Folder already exists", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent =
                    Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createHiddenFolderInDownloads(AppUtils.Main_Folder)
            } else {
                showPermissionSettingsDialog()
            }
        }
    }


    private fun showPermissionSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("Storage permission is required to create folders. Please enable it in app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}