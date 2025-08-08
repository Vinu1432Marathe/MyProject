package com.vinayak.semicolon.securefolderhidefiles.Notes

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log.v
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.R
import com.vinayak.semicolon.securefolderhidefiles.Room.Note

class NoteAdapter(
    val activity: Activity, val onClick: (Note) -> Unit, val onEditClick: (Note) -> Unit,
    val onDeleteClick: (Note) -> Unit
) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var notes = listOf<Note>()

    fun setData(list: List<Note>) {
        notes = list
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val img_Menu = view.findViewById<ImageView>(R.id.img_Menu)

        init {
            view.setOnClickListener {
                onClick(notes[adapterPosition])
            }

            img_Menu.setOnClickListener { v->
                showPopupMenu(v, notes[adapterPosition])
//                showMenuDialog(notes[adapterPosition])

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.tvTitle.text = notes[position].title
        holder.tvContent.text = notes[position].content
    }

    private fun showMenuDialog(note: Note) {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.dialog_note)
        dialog.setCancelable(true)

        val btnEdit = dialog.findViewById<LinearLayout>(R.id.btnEdit)
        val btnDelete = dialog.findViewById<LinearLayout>(R.id.btnDelete)

        btnEdit.setOnClickListener {
            dialog.dismiss()
            onEditClick(note)
        }

        btnDelete.setOnClickListener {
            dialog.dismiss()
            onDeleteClick(note)
        }
        val window = dialog.window
        val wlp = window!!.attributes

        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_BLUR_BEHIND.inv()
        window.attributes = wlp
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dialog.show()

    }

    private fun showPopupMenu(anchor: View, note: Note) {
        val view = LayoutInflater.from(anchor.context).inflate(R.layout.dialog_note, null)
        val popupWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        // Make outside touchable & background transparent
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val edit = view.findViewById<LinearLayout>(R.id.btnEdit)
        val delete = view.findViewById<LinearLayout>(R.id.btnDelete)

        edit.setOnClickListener {
            popupWindow.dismiss()
            onEditClick(note)
        }

        delete.setOnClickListener {
            popupWindow.dismiss()
            onDeleteClick(note)
        }

        // Show the popup aligned to the anchor
        popupWindow.showAsDropDown(anchor, -100, 0) // tweak offset for perfect positioning
    }

}