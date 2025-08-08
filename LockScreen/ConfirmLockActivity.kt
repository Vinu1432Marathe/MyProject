package com.vinayak.semicolon.securefolderhidefiles.LockScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.Activity.MainActivity
import com.vinayak.semicolon.securefolderhidefiles.Other.SharePref
import com.vinayak.semicolon.securefolderhidefiles.R

class ConfirmLockActivity : BaseActivity() {

    private lateinit var pinViews: List<TextView>
    private var enteredPin = ""

    lateinit var Password : String
    lateinit var ComeFrom : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_confirmlock)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Password = intent.getStringExtra("Password").toString()
        ComeFrom = intent.getStringExtra("ComeFrom").toString()

        Log.e("checkkPassword", Password)

        pinViews = listOf(
            findViewById(R.id.pin1),
            findViewById(R.id.pin2),
            findViewById(R.id.pin3),
            findViewById(R.id.pin4)
        )

        val digits = listOf(
            Pair(R.id.key0, "0"), Pair(R.id.key1, "1"), Pair(R.id.key2, "2"),
            Pair(R.id.key3, "3"), Pair(R.id.key4, "4"), Pair(R.id.key5, "5"),
            Pair(R.id.key6, "6"), Pair(R.id.key7, "7"), Pair(R.id.key8, "8"),
            Pair(R.id.key9, "9")
        )

        digits.forEach { (id, digit) ->
            findViewById<Button>(id).setOnClickListener { onDigitPressed(digit) }
        }

        findViewById<ImageView>(R.id.keyDel).setOnClickListener {
            if (enteredPin.isNotEmpty()) {
                enteredPin = enteredPin.dropLast(1)
                updatePinViews()
            }
        }
    }

    private fun onDigitPressed(digit: String) {
        if (enteredPin.length < 4) {
            enteredPin += digit
            updatePinViews()

            if (enteredPin.length == 4) {
                if (Password.equals(enteredPin)){
                    SharePref.setPin(this, enteredPin)
                    Toast.makeText(this, "Your Password Confirm", Toast.LENGTH_SHORT).show()
                    if (ComeFrom.equals("Permission")){
                        val intent = Intent(this, SecurityQuestionActivity::class.java)
                        intent.putExtra("ComeFrom",ComeFrom)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                }else{
                    Toast.makeText(this, "Password not match!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updatePinViews() {
        pinViews.forEachIndexed { index, textView ->
            textView.text = if (index < enteredPin.length) enteredPin[index].toString() else ""
        }
    }
}