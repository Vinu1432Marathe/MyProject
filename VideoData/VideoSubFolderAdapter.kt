package com.vinayak.semicolon.securefolderhidefiles.VideoData

import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vinayak.semicolon.securefolderhidefiles.Model.VideoModel
import com.vinayak.semicolon.securefolderhidefiles.R

class VideoSubFolderAdapter(
    private val videos: List<VideoModel>,
    private val onCheckedChange: (VideoModel) -> Unit
) : RecyclerView.Adapter<VideoSubFolderAdapter.VideoViewHolder>() {

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
      val thumbnail: ImageView = itemView.findViewById(R.id.ivThumbnail)
      val name: TextView = itemView.findViewById(R.id.tvName)
        val checkbox: ImageView = view.findViewById(R.id.ivCheckmark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.name.text = video.name


        Glide.with(holder.thumbnail.context)
            .load(video.uri)
            .thumbnail(0.1f)
            .into(holder.thumbnail)

        val iconRes = if (video.isSelected) R.drawable.ic_check else R.drawable.ic_unchecked
        holder.checkbox.setImageResource(iconRes)

        holder.checkbox.setOnClickListener {
            video.isSelected = !video.isSelected
            notifyItemChanged(position) // refresh icon
            onCheckedChange(video)
        }
//        holder.checkbox.setOnCheckedChangeListener(null)
//        holder.checkbox.isChecked = video.isSelected
//        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
//            video.isSelected = isChecked
//            onCheckedChange(video)
//        }
    }

    override fun getItemCount() = videos.size
}


