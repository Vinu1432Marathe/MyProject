package com.vinayak.semicolon.securefolderhidefiles.LockScreen

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.Activity.MainActivity
import com.vinayak.semicolon.securefolderhidefiles.Other.SharePref
import com.vinayak.semicolon.securefolderhidefiles.R

class SecurityQuestionActivity : BaseActivity() {

    lateinit var txtQuestion : TextView
    lateinit var txtAnswer : TextView
    lateinit var txtSaveQue : TextView
    lateinit var imgQueDown : ImageView
    lateinit var rlQuestion : RelativeLayout
    lateinit var ComeFrom : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_security_question)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgQueDown = findViewById(R.id.imgQueDown)
        txtQuestion = findViewById(R.id.txtQuestion)
        txtAnswer = findViewById(R.id.txtAnswer)
        rlQuestion = findViewById(R.id.rlQuestion)
        txtSaveQue = findViewById(R.id.txtSaveQue)

        val savedQuestion = SharePref.getSecurityQuestion(this)
        val savedAnswer = SharePref.getSecurityAnswer(this)
        if (savedQuestion?.isEmpty() == false || savedAnswer?.isEmpty() == false){
            txtQuestion.text = savedQuestion
        }

        ComeFrom = intent.getStringExtra("ComeFrom").toString()
        rlQuestion.setOnClickListener {
            addQuesiton()
        }


        txtSaveQue.setOnClickListener {

            val Que = txtQuestion.text.toString()
            val Ans = txtAnswer.text.toString()



            if (!Ans.isEmpty()){
                if (ComeFrom.equals("Permission")){

                    SharePref.setSecurityQuestion(this, Que, Ans)
                    val intent = Intent(this, MainLockSceenActivity::class.java)
                    startActivity(intent)

                }else{

                    SharePref.setSecurityQuestion(this, Que, Ans)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                SharePref.setOnboarding(this, true)

            }else{

                Toast.makeText(this,"Please enter Answer!!", Toast.LENGTH_SHORT).show()
            }


        }

    }


    private fun addQuesiton() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.dialog_add_question)
        dialog.setCancelable(true)

        val txtQue1 = dialog.findViewById<TextView>(R.id.txtQue1)
        val txtQue2 = dialog.findViewById<TextView>(R.id.txtQue2)
        val txtQue3 = dialog.findViewById<TextView>(R.id.txtQue3)
        val txtQue4 = dialog.findViewById<TextView>(R.id.txtQue4)
        val txtQue5 = dialog.findViewById<TextView>(R.id.txtQue5)

        txtQue1?.setOnClickListener {

            txtQuestion.text = getString(R.string.what_is_the_name_of_your_favorite_friend)
            dialog.dismiss()
        }
        txtQue2?.setOnClickListener {
            txtQuestion.text = getString(R.string.what_was_your_favorite_childhood_cartoon)
            dialog.dismiss()
        }
        txtQue3?.setOnClickListener {
            txtQuestion.text = getString(R.string.what_was_your_childhood_nickname)
            dialog.dismiss()

        }
        txtQue4?.setOnClickListener {
            txtQuestion.text = getString(R.string.what_was_your_favorite_food)
            dialog.dismiss()

        }
        txtQue5?.setOnClickListener {

            txtQuestion.text = getString(R.string.what_is_your_favorite_movie)
            dialog.dismiss()

        }


        dialog.show()
    }
}