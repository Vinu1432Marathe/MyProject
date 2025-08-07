package com.vinayak.semicolon.securefolderhidefiles.ContactData

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.R
import com.vinayak.semicolon.securefolderhidefiles.Room.ContactViewModel

class ViewContactActivity : BaseActivity() {


    private lateinit var viewModel: ContactViewModel

    lateinit var contactID : String
    lateinit var contactName : String
    lateinit var contactNumber : String

    lateinit var txtUserName : TextView
    lateinit var txtMobileNo : TextView
    lateinit var llUnhide : LinearLayout
    lateinit var llDelete : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_view_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]

        txtUserName = findViewById(R.id.txtUserName)
        txtMobileNo = findViewById(R.id.txtMobileNo)
        llUnhide = findViewById(R.id.llUnhide)
        llDelete = findViewById(R.id.llDelete)

        contactID = intent.getStringExtra("Id").toString()
        contactName = intent.getStringExtra("Name").toString()
        contactNumber = intent.getStringExtra("Number").toString()

        txtUserName.text = contactName
        txtMobileNo.text = contactNumber

        Log.e("Data","Data1 :$contactID")
        Log.e("Data","Data2 :$contactName")
        Log.e("Data","Data3 :$contactNumber")

        llUnhide.setOnClickListener {


        }

        llDelete.setOnClickListener {

            if (!contactID.isEmpty()){
                viewModel.deleteContactById( contactID)
            }
            finish()

        }


    }
}