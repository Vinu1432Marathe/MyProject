package com.vinayak.semicolon.securefolderhidefiles.Activity

import android.R.attr.path
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Model.FolderItem
import com.vinayak.semicolon.securefolderhidefiles.R

class MoveDataFolderAdapter(
    var activity: Activity,
    private var folders: List<FolderItem>,
    private val onCheckedChange: (path: String) -> Unit
) : RecyclerView.Adapter<MoveDataFolderAdapter.FolderViewHolder>() {

    var pos: Int = -1

    class FolderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.folderName)
        val fileCount: TextView = view.findViewById(R.id.fileCount)
        val moreIcon: ImageView = view.findViewById(R.id.moreIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_movefolder, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folders[position]
        holder.tvName.text = folder.name
        holder.fileCount.text = folder.fileCount.toString() + " Items"

        if (pos.equals(position)) {
            holder.moreIcon.setImageResource(R.drawable.ic_check)
        } else {
            holder.moreIcon.setImageResource(R.drawable.ic_unchecked)
        }


        holder.itemView.setOnClickListener {
            Log.e("CheckMove", "Move Adapter :: $path")
            pos = position
            onCheckedChange(folder.path)
            notifyDataSetChanged()

        }
    }

    override fun getItemCount() = folders.size

    fun updateList(newFolders: List<FolderItem>) {
        folders = newFolders
        notifyDataSetChanged()
    }
}