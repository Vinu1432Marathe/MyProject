package com.vinayak.semicolon.securefolderhidefiles.Activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vinayak.semicolon.securefolderhidefiles.LockScreen.LockSceenActivity
import com.vinayak.semicolon.securefolderhidefiles.LockScreen.SecurityQuestionActivity
import com.vinayak.semicolon.securefolderhidefiles.Other.AppUtils
import com.vinayak.semicolon.securefolderhidefiles.Other.SharePref
import com.vinayak.semicolon.securefolderhidefiles.OtherFeature.DisguiseModeActivity
import com.vinayak.semicolon.securefolderhidefiles.R
import kotlin.jvm.java

class SettingActivity : BaseActivity() {

    lateinit var rlLanguage : RelativeLayout
    lateinit var rlDisguised : RelativeLayout
    lateinit var rlPassword : RelativeLayout
    lateinit var rlChangEmail : RelativeLayout
    lateinit var rlChangQue : RelativeLayout
    lateinit var rlUninstall : RelativeLayout
    lateinit var rlShareApp : RelativeLayout
    lateinit var rlRateApp : RelativeLayout
    lateinit var rlPrivacy : RelativeLayout
    lateinit var rlFinger : RelativeLayout
    lateinit var txtVersion : TextView
    lateinit var swThemeMode: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_setting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rlLanguage = findViewById(R.id.rlLanguage)
        rlDisguised = findViewById(R.id.rlDisguised)
        rlPassword = findViewById(R.id.rlPassword)
        rlChangEmail = findViewById(R.id.rlChangEmail)
        rlChangQue = findViewById(R.id.rlChangQue)
        rlUninstall = findViewById(R.id.rlUninstall)
        rlShareApp = findViewById(R.id.rlShareApp)
        rlRateApp = findViewById(R.id.rlRateApp)
        rlPrivacy = findViewById(R.id.rlPrivacy)
        rlFinger = findViewById(R.id.rlFinger)
        txtVersion = findViewById(R.id.txtVersion)
        swThemeMode = findViewById(R.id.swThemeMode)

        checkFingerprintAvailability(this)


        var versionName = ""

        try {
            val pm = packageManager
            val pInfo = pm.getPackageInfo(packageName, 0)
            versionName = pInfo.versionName.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        txtVersion.text = versionName

        swThemeMode.isChecked = SharePref.getSavedTheme(this) == SharePref.THEME_DARK

        swThemeMode.setOnCheckedChangeListener { _, isChecked ->
            val selectedTheme = if (isChecked) SharePref.THEME_DARK else SharePref.THEME_LIGHT
            SharePref.saveTheme(this, selectedTheme)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        rlLanguage.setOnClickListener {

        }

        rlDisguised.setOnClickListener {

            val intent = Intent(this, DisguiseModeActivity::class.java)
            startActivity(intent)
        }

        rlPassword.setOnClickListener {

            val intent = Intent(this, LockSceenActivity::class.java)
            intent.putExtra("comeFrom","Setting")
            startActivity(intent)
        }

        rlChangEmail.setOnClickListener {

        }

        rlChangQue.setOnClickListener {


            val intent = Intent(this, SecurityQuestionActivity::class.java)
            intent.putExtra("comeFrom","Setting")
            startActivity(intent)
        }

        rlUninstall.setOnClickListener {

            val intent = Intent(this, UninstallProtectionActivity::class.java)
            startActivity(intent)

        }

        rlShareApp.setOnClickListener {
            AppUtils.shareApp(this)
        }

        rlRateApp.setOnClickListener {
            AppUtils.rateUs(this)
        }

        rlPrivacy.setOnClickListener {

            AppUtils.openPrivacy(this)

        }

    }

    fun checkFingerprintAvailability(context: Context) {
        val biometricManager = BiometricManager.from(context)

        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                rlFinger.visibility = View.VISIBLE
//                Toast.makeText(context, "Fingerprint available and enrolled", Toast.LENGTH_SHORT).show()

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                rlFinger.visibility = View.GONE
//                Toast.makeText(context, "No biometric hardware available", Toast.LENGTH_SHORT).show()

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                rlFinger.visibility = View.GONE
//                Toast.makeText(context, "Biometric hardware currently unavailable", Toast.LENGTH_SHORT).show()

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                rlFinger.visibility = View.GONE
//                Toast.makeText(context, "No fingerprint enrolled", Toast.LENGTH_SHORT).show()
        }
    }
}