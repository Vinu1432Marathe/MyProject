package com.vinayak.semicolon.securefolderhidefiles.OtherFeature

import android.content.ComponentName
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.Other.SharePref.getSharedPreferences
import com.vinayak.semicolon.securefolderhidefiles.R

class DisguiseModeActivity : BaseActivity() {

    lateinit var imgLogoMain: ImageView
    lateinit var imgLogoMain1: ImageView
    lateinit var txtMainLogo: TextView
    lateinit var txtMainLogo1: TextView
    lateinit var txtAppLogo: TextView

    lateinit var llLevel1: LinearLayout
    lateinit var llLevel2: LinearLayout
    lateinit var llLevel3: LinearLayout
    lateinit var llLevel4: LinearLayout
    lateinit var llLevel5: LinearLayout
    lateinit var llLevel6: LinearLayout

    lateinit var imgCheck1: ImageView
    lateinit var imgCheck2: ImageView
    lateinit var imgCheck3: ImageView
    lateinit var imgCheck4: ImageView
    lateinit var imgCheck5: ImageView
    lateinit var imgCheck6: ImageView

    lateinit var logoNu: String

    private val aliases = listOf(
        "com.vinayak.semicolon.securefolderhidefiles.Alias1",
        "com.vinayak.semicolon.securefolderhidefiles.Alias2",
        "com.vinayak.semicolon.securefolderhidefiles.Alias3",
        "com.vinayak.semicolon.securefolderhidefiles.Alias4",
        "com.vinayak.semicolon.securefolderhidefiles.Alias5",
        "com.vinayak.semicolon.securefolderhidefiles.Alias6"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disguise_mode)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgLogoMain = findViewById(R.id.imgLogoMain)
        imgLogoMain1 = findViewById(R.id.imgLogoMain1)
        txtMainLogo = findViewById(R.id.txtMainLogo)
        txtMainLogo1 = findViewById(R.id.txtMainLogo1)
        txtAppLogo = findViewById(R.id.txtAppLogo)

        llLevel1 = findViewById(R.id.llLevel1)
        llLevel2 = findViewById(R.id.llLevel2)
        llLevel3 = findViewById(R.id.llLevel3)
        llLevel4 = findViewById(R.id.llLevel4)
        llLevel5 = findViewById(R.id.llLevel5)
        llLevel6 = findViewById(R.id.llLevel6)

        imgCheck1 = findViewById(R.id.imgCheck1)
        imgCheck2 = findViewById(R.id.imgCheck2)
        imgCheck3 = findViewById(R.id.imgCheck3)
        imgCheck4 = findViewById(R.id.imgCheck4)
        imgCheck5 = findViewById(R.id.imgCheck5)
        imgCheck6 = findViewById(R.id.imgCheck6)


        Log.e("checkkLOGO", "LOGO :: ${getCurrentLogo()}")

        when (getCurrentLogo()) {

            "Alias" -> {

                imgLogoMain.setImageResource(R.drawable.s_logo)
                txtMainLogo.text = getString(R.string.secure_vault_pro)

            }

            "Alias1" -> {
                imgLogoMain.setImageResource(R.drawable.logo1)
                txtMainLogo.text = getString(R.string.calculator)

            }

            "Alias2" -> {
                imgLogoMain.setImageResource(R.drawable.logo2)
                txtMainLogo.text = getString(R.string.clock)

            }

            "Alias3" -> {
                imgLogoMain.setImageResource(R.drawable.logo3)
                txtMainLogo.text = getString(R.string.notes)

            }

            "Alias4" -> {
                imgLogoMain.setImageResource(R.drawable.logo4)
                txtMainLogo.text = getString(R.string.file_manager)

            }

            "Alias5" -> {
                imgLogoMain.setImageResource(R.drawable.logo5)
                txtMainLogo.text = getString(R.string.voice_recorder)

            }

            "Alias6" -> {
                imgLogoMain.setImageResource(R.drawable.logo6)
                txtMainLogo.text = getString(R.string.weather)

            }

        }


        llLevel1.setOnClickListener {

            imgCheck1.visibility = View.VISIBLE
            imgCheck2.visibility = View.GONE
            imgCheck3.visibility = View.GONE
            imgCheck4.visibility = View.GONE
            imgCheck5.visibility = View.GONE
            imgCheck6.visibility = View.GONE
            imgLogoMain1.setImageResource(R.drawable.logo1)
            txtMainLogo1.text = getString(R.string.calculator)
            logoNu = aliases[0]
        }

        llLevel2.setOnClickListener {

            imgCheck1.visibility = View.GONE
            imgCheck2.visibility = View.VISIBLE
            imgCheck3.visibility = View.GONE
            imgCheck4.visibility = View.GONE
            imgCheck5.visibility = View.GONE
            imgCheck6.visibility = View.GONE
            imgLogoMain1.setImageResource(R.drawable.logo2)
            txtMainLogo1.text = getString(R.string.clock)
            logoNu = aliases[1]
        }

        llLevel3.setOnClickListener {

            imgCheck1.visibility = View.GONE
            imgCheck2.visibility = View.GONE
            imgCheck3.visibility = View.VISIBLE
            imgCheck4.visibility = View.GONE
            imgCheck5.visibility = View.GONE
            imgCheck6.visibility = View.GONE
            imgLogoMain1.setImageResource(R.drawable.logo3)
            txtMainLogo1.text = getString(R.string.notes)
            logoNu = aliases[2]
        }

        llLevel4.setOnClickListener {

            imgCheck1.visibility = View.GONE
            imgCheck2.visibility = View.GONE
            imgCheck3.visibility = View.GONE
            imgCheck4.visibility = View.VISIBLE
            imgCheck5.visibility = View.GONE
            imgCheck6.visibility = View.GONE
            imgLogoMain1.setImageResource(R.drawable.logo4)
            txtMainLogo1.text = getString(R.string.file_manager)
            logoNu = aliases[3]
        }

        llLevel5.setOnClickListener {
            imgCheck1.visibility = View.GONE
            imgCheck2.visibility = View.GONE
            imgCheck3.visibility = View.GONE
            imgCheck4.visibility = View.GONE
            imgCheck5.visibility = View.VISIBLE
            imgCheck6.visibility = View.GONE
            imgLogoMain1.setImageResource(R.drawable.logo5)
            txtMainLogo1.text = getString(R.string.voice_recorder)
            logoNu = aliases[4]

        }

        llLevel6.setOnClickListener {

            imgCheck1.visibility = View.GONE
            imgCheck2.visibility = View.GONE
            imgCheck3.visibility = View.GONE
            imgCheck4.visibility = View.GONE
            imgCheck5.visibility = View.GONE
            imgCheck6.visibility = View.VISIBLE
            imgLogoMain1.setImageResource(R.drawable.logo6)
            txtMainLogo1.text = getString(R.string.weather)
            logoNu = aliases[5]
        }


        txtAppLogo.setOnClickListener {

            if (isAnyImageVisible()) {

                switchAppIcon(logoNu)
                Toast.makeText(this, "Logo set successfully", Toast.LENGTH_SHORT)
                    .show()

            } else {
                Toast.makeText(this, "Select logo first..", Toast.LENGTH_SHORT)
                    .show()
            }


        }

    }

    private fun isAnyImageVisible(): Boolean {
        return imgCheck1.isVisible ||
                imgCheck2.isVisible ||
                imgCheck3.isVisible ||
                imgCheck4.isVisible ||
                imgCheck5.isVisible ||
                imgCheck6.isVisible


    }

    private fun switchAppIcon(enableAlias: String) {
        val pm = packageManager
        val prefs = getSharedPreferences("icon_prefs", MODE_PRIVATE)
        aliases.forEach { alias ->
            pm.setComponentEnabledSetting(
                ComponentName(this, alias),
                if (alias == enableAlias)
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                else
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
        prefs.edit().putString("current_icon", enableAlias).apply()
    }

    private fun getCurrentLogo(): String {
        val prefs = getSharedPreferences("icon_prefs", MODE_PRIVATE)
        return prefs.getString("current_icon", aliases[0])?.substringAfterLast('.') ?: "Alias"
    }
}