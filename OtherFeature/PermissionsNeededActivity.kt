package com.vinayak.semicolon.securefolderhidefiles.OtherFeature

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.R

class PermissionsNeededActivity : BaseActivity() {

    private val permissions = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.CAMERA
    )

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val deniedPermissions = result.filter { !it.value }.map { it.key }

            if (deniedPermissions.isEmpty()) {
                Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                val permanentlyDenied = deniedPermissions.any { permission ->
                    !ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
                }

                if (permanentlyDenied) {
                    showSettingsDialog()
                } else {
                    Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    lateinit var btnEnable : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_permissions_needed)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnEnable = findViewById(R.id.btnEnable)

        btnEnable.setOnClickListener {
            checkAndRequestPermissions()

        }
    }
    private fun checkAndRequestPermissions() {
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isEmpty()) {
            Toast.makeText(this,
                getString(R.string.all_permissions_already_granted), Toast.LENGTH_SHORT).show()
        } else {
            permissionLauncher.launch(notGranted.toTypedArray())
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.permissions_required))
            .setMessage(getString(R.string.some_permissions_are_permanently_denied_please_enable_them_from_app_settings))
            .setPositiveButton(getString(R.string.go_to_settings)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }
}