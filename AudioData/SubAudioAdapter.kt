package com.vinayak.semicolon.securefolderhidefiles.AudioData

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Model.AudioModel
import com.vinayak.semicolon.securefolderhidefiles.R

class SubAudioAdapter(
    private val list: List<AudioModel>,
    private val onItemSelected: (AudioModel) -> Unit
) : RecyclerView.Adapter<SubAudioAdapter.AudioViewHolder>() {

    inner class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.audioTitle)
        val artist: TextView = itemView.findViewById(R.id.audioArtist)

        //        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val imgSelect: ImageView = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adpter_audio, parent, false)
        return AudioViewHolder(view)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.artist.text = item.artist

        holder.imgSelect.setImageResource(
            if (item.isSelected) R.drawable.ic_check else R.drawable.ic_unchecked
        )

        holder.imgSelect.setOnClickListener {
            item.isSelected = !item.isSelected
            notifyItemChanged(position)
            onItemSelected(item)
        }

//        holder.checkBox.setOnCheckedChangeListener(null)
//        holder.checkBox.isChecked = item.isSelected
//
//        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
//            item.isSelected = isChecked
//            onItemSelected(item)
//        }
    }

    override fun getItemCount(): Int = list.size
}