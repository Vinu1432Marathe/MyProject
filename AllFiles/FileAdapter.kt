package com.vinayak.semicolon.securefolderhidefiles.AllFiles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vinayak.semicolon.securefolderhidefiles.Model.AudioModel
import com.vinayak.semicolon.securefolderhidefiles.Model.FileModel
import com.vinayak.semicolon.securefolderhidefiles.R
import java.io.File
import java.text.DecimalFormat

class FileAdapter(private val files: List<FileModel>,
                  private val onItemSelected: (FileModel) -> Unit ) :
    RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    inner class FileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fileName: TextView = view.findViewById(R.id.folderName)
        val fileSize: TextView = view.findViewById(R.id.fileCount)
        val folderIcon: ImageView = view.findViewById(R.id.folderIcon)
        val moreIcon: ImageView = view.findViewById(R.id.moreIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.fileName.text = file.name
        holder.fileSize.text = readableFileSize(file.size)

        val name = file.name.lowercase()
        when {
            name.contains(".jpg") || name.contains(".jpeg") || name.contains(".png")|| name.contains(".webp") -> {
               val  image = file.path
                Glide.with(holder.folderIcon.context)
                    .load(file.path)
                    .placeholder(R.drawable.ic_image)
                    .into(holder.folderIcon)

            }
            name.contains(".mp4") || name.contains(".mkv")|| name.contains(".avi")|| name.contains(".3gp") -> {
                Glide.with(holder.folderIcon.context)
                    .load(file.path)
                    .placeholder(R.drawable.ic_video)
                    .into(holder.folderIcon)
            }
            name.contains(".mp3") || name.contains(".wav")|| name.contains(".aac")|| name.contains(".flac")|| name.contains(".ogg")|| name.contains(".m4a") -> {
                holder.folderIcon.setImageResource(R.drawable.ic_music)
            }
            name.contains(".pdf") || name.contains(".docx") || name.contains(".txt")|| name.contains(".doc")|| name.contains(".ppt")
                    || name.contains(".pptx")|| name.contains(".xls")|| name.contains(".xlsx") -> {
                holder.folderIcon.setImageResource(R.drawable.ic_item_doc)
            }
        }

        holder.moreIcon.setImageResource(
            if (file.isSelected) R.drawable.ic_check else R.drawable.ic_unchecked
        )

        holder.itemView.setOnClickListener {
            file.isSelected = !file.isSelected
            notifyItemChanged(position)
            onItemSelected(file)
        }


    }

    override fun getItemCount() = files.size

    private fun readableFileSize(size: Long): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }
}
