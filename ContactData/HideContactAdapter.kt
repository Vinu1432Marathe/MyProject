package com.vinayak.semicolon.securefolderhidefiles.ContactData

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.R
import com.vinayak.semicolon.securefolderhidefiles.Room.ContactEntity

class HideContactAdapter(
    private val onItemClick: (id: String, name: String, number: String) -> Unit,
    private val onCallClick: (String) -> Unit,
    private val onMessageClick: (String) -> Unit
) : RecyclerView.Adapter<HideContactAdapter.ViewHolder>() {

    private val contactList = mutableListOf<ContactEntity>()

    fun submitList(list: List<ContactEntity>) {
        contactList.clear()
        contactList.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.tvName)
        val number = view.findViewById<TextView>(R.id.tvNumber)
        val img_Call: ImageView = view.findViewById(R.id.img_Call)
        val img_Msg: ImageView = view.findViewById(R.id.img_Msg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_hide_contact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = contactList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = contactList[position]
        holder.name.text = contact.name
        holder.number.text = contact.number

        holder.itemView.setOnClickListener {
            onItemClick(contact.contactId, contact.name, contact.number)
        }

        holder.img_Msg.setOnClickListener {
            onMessageClick(contact.number)
        }
        holder.img_Call.setOnClickListener {
            onCallClick(contact.number)
        }
    }
}

//
//class HideContactAdapter(private val contacts: List<SavedContact>,   private val onCheckedChanged: (Contact) -> Unit,private val onCallClick: (String) -> Unit,
//                         private val onMessageClick: (String) -> Unit ) :
//    RecyclerView.Adapter<HideContactAdapter.ViewHolder>() {
//    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val name: TextView = view.findViewById(R.id.tvName)
//        val number: TextView = view.findViewById(R.id.tvNumber)
//        val img_Call: ImageView = view.findViewById(R.id.img_Call)
//        val img_Msg: ImageView = view.findViewById(R.id.img_Msg)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.adapter_hide_contact, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val contact = contacts[position]
//        holder.name.text = contact.name
//        holder.number.text = contact.number
//        holder.img_Msg.setOnClickListener {
//            onMessageClick(contact.number)
//        }
//        holder.img_Call.setOnClickListener {
//            onCallClick(contact.number)
//        }
//    }
//
//    override fun getItemCount(): Int = contacts.size
//}