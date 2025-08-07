package com.vinayak.semicolon.securefolderhidefiles.DocumentData

import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Model.DocumentModel
import com.vinayak.semicolon.securefolderhidefiles.R
import java.nio.file.Files.size

class DocSubFolderAdapter(private var items: MutableList<DocumentModel>, private val onToggle: (Int) -> Unit) :
    RecyclerView.Adapter<DocSubFolderAdapter.DocumentViewHolder>() {

    inner class DocumentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvSize: TextView = itemView.findViewById(R.id.tvSize)
        val checkIcon: ImageView = view.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_document, parent, false)
        return DocumentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val item = items[position]
        holder.tvName.text = item.name

        holder.tvSize.text = buildString {
        append(item.size / 1024)
        append(" KB")
    }
        holder.checkIcon.setImageResource(
            if (item.isSelected) R.drawable.ic_check else R.drawable.ic_unchecked
        )

        holder.itemView.setOnClickListener {

            onToggle(position)
        }
    }

    override fun getItemCount() = items.size

    fun updateData(newList: List<DocumentModel>) {
        items = newList.toMutableList()
        notifyDataSetChanged()
    }
}
