package com.vinayak.semicolon.securefolderhidefiles.AudioData

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.Model.AudioModel
import com.vinayak.semicolon.securefolderhidefiles.R

class AudioAdapter(
    private var audioList: List<AudioModel>,
    private val onSelectionChanged: () -> Unit
) : RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    inner class AudioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.audioTitle)
//        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)
        val imgSelect = itemView.findViewById<ImageView>(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adpter_audio, parent, false)
        return AudioViewHolder(view)
    }


    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audio = audioList[position]
        holder.title.text = audio.title


//        holder.checkBox.isChecked = audio.isSelected
//        Log.e("checkkAudio", " From :: ${audio.data}")
//        holder.checkBox.setOnCheckedChangeListener(null)
//        holder.checkBox.isChecked = audio.isSelected
//        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
//
//            if (audio.isSelected != isChecked) {
//                audio.isSelected = isChecked
//                onSelectionChanged()
//            }
//
//        }

        holder.imgSelect.setImageResource(
            if (audio.isSelected) R.drawable.ic_check else R.drawable.ic_unchecked
        )


        holder.imgSelect.setOnClickListener {
            audio.isSelected = !audio.isSelected
            notifyItemChanged(position)
//            onItemSelected(audio)
            onSelectionChanged()
        }

        holder.itemView.setOnClickListener {
            audio.isSelected = !audio.isSelected
            notifyItemChanged(position)
            onSelectionChanged()
        }

    }

    override fun getItemCount() = audioList.size


    fun updateList(newList: List<AudioModel>) {
        audioList = newList
        notifyDataSetChanged()
    }

    fun getSelectedItems(): List<AudioModel> = audioList.filter { it.isSelected }
}


