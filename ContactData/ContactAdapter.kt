package com.vinayak.semicolon.securefolderhidefiles.ContactData

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Model.DeviceContact
import com.vinayak.semicolon.securefolderhidefiles.R

class ContactAdapter(
    private val contactList: List<DeviceContact>,
    private val onCheckedChange: (DeviceContact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.tvName)
        val number = view.findViewById<TextView>(R.id.tvNumber)
        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactList[position]
        holder.name.text = contact.name
        holder.number.text = contact.number
        holder.checkBox.isChecked = contact.isSelected

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            contact.isSelected = isChecked
            onCheckedChange(contact)
        }
    }

    override fun getItemCount(): Int = contactList.size
}


//class ContactAdapter(
//    private val contacts: List<Contact>,
//    private val onCheckedChanged: (Contact) -> Unit
//) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
//
//    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val nameTV: TextView = itemView.findViewById(R.id.tvName)
//        val numberTV: TextView = itemView.findViewById(R.id.tvNumber)
//        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_contact, parent, false)
//        return ContactViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
//        val contact = contacts[position]
//        holder.nameTV.text = contact.name
//        holder.numberTV.text = contact.number
//        holder.checkBox.setOnCheckedChangeListener(null)
//        holder.checkBox.isChecked = contact.isSelected
//        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
//            contact.isSelected = isChecked
//            onCheckedChanged(contact)
//        }
//    }
//
//    override fun getItemCount(): Int = contacts.size
//}
