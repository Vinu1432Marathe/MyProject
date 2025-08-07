package com.vinayak.semicolon.securefolderhidefiles.ContactData

import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Activity.BaseActivity
import com.vinayak.semicolon.securefolderhidefiles.Model.DeviceContact
import com.vinayak.semicolon.securefolderhidefiles.R
import com.vinayak.semicolon.securefolderhidefiles.Room.ContactEntity
import com.vinayak.semicolon.securefolderhidefiles.Room.ContactViewModel

class ContactListActivity : BaseActivity() {

    private lateinit var txtSelectSize: TextView
    private lateinit var rlNoData: RelativeLayout
    private lateinit var rlViewData: RelativeLayout
    private lateinit var viewModel: ContactViewModel
    private val deviceContacts = mutableListOf<DeviceContact>()
    private val selectedContacts = mutableListOf<DeviceContact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_contact_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val rlHideData = findViewById<RelativeLayout>(R.id.rlHideData)
        rlNoData = findViewById(R.id.rlNoData)
        txtSelectSize = findViewById(R.id.txtSelectSize)
        rlViewData = findViewById(R.id.rlViewData)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load contacts from device
        loadDeviceContacts()
        val adapter = ContactAdapter(deviceContacts) {
            if (it.isSelected) selectedContacts.add(it) else selectedContacts.remove(it)
            val count = selectedContacts.size
            txtSelectSize.text = "("+count.toString()+")"
        }
        recyclerView.adapter = adapter

        // Save to DB
        rlHideData.setOnClickListener {
            val entities = selectedContacts.map {
                ContactEntity(it.id, it.name, it.number)
            }
            viewModel.insertContacts(entities)
            for (contact in selectedContacts) {
                viewModel.deleteFromDevice(this, contact.id)
            }
            loadDeviceContacts()
            recyclerView.adapter?.notifyDataSetChanged()
            finish()
        }


        // Observe saved Room DB
        viewModel.savedContacts.observe(this) {
            if (it.isEmpty()) {
                rlViewData.visibility = View.GONE
                rlNoData.visibility = View.VISIBLE

            } else {
                rlViewData.visibility = View.VISIBLE
                rlNoData.visibility = View.GONE

            }

        }
    }

    private fun loadDeviceContacts() {
        deviceContacts.clear()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )
        cursor?.use {
            val idIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val id = it.getString(idIdx)
                val name = it.getString(nameIdx)
                val number = it.getString(numIdx)
                deviceContacts.add(DeviceContact(id, name, number))
            }
        }
    }


}