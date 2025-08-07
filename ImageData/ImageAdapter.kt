package com.vinayak.semicolon.securefolderhidefiles.ImageData

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vinayak.semicolon.securefolderhidefiles.Model.ImageModel
import com.vinayak.semicolon.securefolderhidefiles.R

class ImageAdapter(
    private var images: List<ImageModel>,
    private val onItemSelected: (ImageModel) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivThumbnail)
        val nameView: TextView = itemView.findViewById(R.id.tvName)
        val sizeView: TextView = itemView.findViewById(R.id.tvSize)
        val checkBox: ImageView = itemView.findViewById(R.id.cbSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        holder.imageView.setImageURI(image.uri)
        holder.nameView.text = image.name
        holder.sizeView.text = formatSize(image.size)

        val iconRes = if (image.isSelected) R.drawable.ic_check else R.drawable.ic_unchecked
        holder.checkBox.setImageResource(iconRes)

        holder.checkBox.setOnClickListener {
            image.isSelected = !image.isSelected
            notifyItemChanged(position) // refresh icon
            onItemSelected(image)
        }
//        holder.checkBox.isChecked = image.isSelected
//
//        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
//            images[position].isSelected = isChecked
//            onItemSelected(image)
//        }
    }

    override fun getItemCount() = images.size

    fun updateList(newList: List<ImageModel>) {
        images = newList
        notifyDataSetChanged()
    }

    private fun formatSize(bytes: Long): String {
        val kb = bytes / 1024
        val mb = kb / 1024
        return if (mb > 0) "$mb MB" else "$kb KB"
    }
}
