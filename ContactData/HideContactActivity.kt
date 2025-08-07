package com.vinayak.semicolon.securefolderhidefiles.ContactData

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.R
import com.vinayak.semicolon.securefolderhidefiles.Room.ContactDatabase
import com.vinayak.semicolon.securefolderhidefiles.Room.ContactViewModel
import com.vinayak.semicolon.securefolderhidefiles.VideoData.VideoFolderActivity
import kotlinx.coroutines.launch

class HideContactActivity : BaseActivity() {

    lateinit var imgCreate: ImageView
    lateinit var rlNoData: RelativeLayout
    lateinit var hideContactAdapter: HideContactAdapter

    private lateinit var viewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_hide_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgCreate = findViewById(R.id.imgCreate)
        rlNoData = findViewById(R.id.rlNoData)

        imgCreate.setOnClickListener {
            folderCreate()
        }


        val recyclerView = findViewById<RecyclerView>(R.id.rvFolder)
        recyclerView.layoutManager = LinearLayoutManager(this)

        hideContactAdapter = HideContactAdapter(

            onItemClick = { id ,name,number ->
                val intent = Intent(this@HideContactActivity, ViewContactActivity::class.java)
                    .putExtra("Id",id)
                    .putExtra("Name",name)
                    .putExtra("Number",number)
                startActivity(intent)

            },

            onCallClick = { number ->

                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$number")
                startActivity(intent)
            },
            onMessageClick = { number ->
                val smsIntent = Intent(Intent.ACTION_VIEW)
                smsIntent.data = Uri.parse("smsto:$number")
                smsIntent.putExtra("sms_body", "Hello from my app!")
                startActivity(smsIntent)
            }
        )


        recyclerView.adapter = hideContactAdapter

        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]

        viewModel.savedContacts.observe(this) { savedContacts ->
            if (savedContacts.isEmpty()){
                recyclerView.visibility = View.GONE
                rlNoData.visibility = View.VISIBLE

            }else{
                recyclerView.visibility = View.VISIBLE
                rlNoData.visibility = View.GONE
                hideContactAdapter.submitList(savedContacts)

            }
        }
    }





    private fun folderCreate() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.dialog_contact)
        dialog.setCancelable(true)

        val btnContact = dialog.findViewById<TextView>(R.id.btnContact)
        val btnAddContact = dialog.findViewById<TextView>(R.id.btnAddContact)

        btnAddContact?.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        btnContact?.setOnClickListener {
            val intent = Intent(this, ContactListActivity::class.java)
            startActivity(intent)
            dialog.dismiss()
        }

        dialog.show()
    }
}