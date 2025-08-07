package com.vinayak.semicolon.securefolderhidefiles.ContactData

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.Model.DeviceContact
import com.vinayak.semicolon.securefolderhidefiles.R
import com.vinayak.semicolon.securefolderhidefiles.Room.ContactDatabase
import com.vinayak.semicolon.securefolderhidefiles.Room.ContactEntity
import com.vinayak.semicolon.securefolderhidefiles.Room.ContactViewModel
import kotlinx.coroutines.launch
import java.util.UUID

class AddContactActivity : BaseActivity() {


    private lateinit var viewModel: ContactViewModel
    lateinit var txtUserName : EditText
    lateinit var txtMobileNo : EditText
    lateinit var imgBack : ImageView
    lateinit var btnSave : TextView

    private val selectedContacts = mutableListOf<DeviceContact>()

    private lateinit var db: ContactDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_contact)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]

        txtUserName = findViewById(R.id.txtUserName)
        txtMobileNo = findViewById(R.id.txtMobileNo)
        btnSave = findViewById(R.id.btnSave)
        imgBack = findViewById(R.id.imgBack)

//        db = ContactDatabase.getDatabase(this)

        imgBack.setOnClickListener { onBackPressed() }




        btnSave.setOnClickListener {
            val name = txtUserName.text.toString().trim()
            val number = txtMobileNo.text.toString().trim()

            if (name.isNotEmpty() && number.isNotEmpty()) {
                val contactId = UUID.randomUUID().toString() // unique id
                val contact = ContactEntity(contactId, name, number)
                viewModel.insertContact(contact)
                finish()
            } else {
                Toast.makeText(this, "Please enter name and number", Toast.LENGTH_SHORT).show()
            }
        }

    }
}