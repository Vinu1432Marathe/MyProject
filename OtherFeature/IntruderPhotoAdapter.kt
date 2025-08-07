package com.vinayak.semicolon.securefolderhidefiles.OtherFeature

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IntruderPhotoAdapter(private val photoList: List<File>) :
    RecyclerView.Adapter<IntruderPhotoAdapter.PhotoViewHolder>() {

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivThumbnail: ImageView = itemView.findViewById(R.id.ivThumbnail)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvSize: TextView = itemView.findViewById(R.id.tvSize)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_intruder_photo, parent, false)
        return PhotoViewHolder(view)

    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val file = photoList[position]
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        holder.ivThumbnail.setImageBitmap(bitmap)

        // Format date and time from file.lastModified()
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val sdtt = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val dateTime = sdf.format(Date(file.lastModified()))
        val dateTimett = sdtt.format(Date(file.lastModified()))
        holder.tvName.text  = dateTime
        holder.tvSize.text  = dateTimett
    }

    override fun getItemCount(): Int = photoList.size
}
