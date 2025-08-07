package com.vinayak.semicolon.securefolderhidefiles.AllFiles

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vinayak.semicolon.securefolderhidefiles.AudioData.AudioSubFolderActivity
import com.vinayak.semicolon.securefolderhidefiles.DocumentData.DocSubFolderActivity
import com.vinayak.semicolon.securefolderhidefiles.ImageData.IMGSubFolderActivity
import com.vinayak.semicolon.securefolderhidefiles.Model.FolderItem
import com.vinayak.semicolon.securefolderhidefiles.R
import com.vinayak.semicolon.securefolderhidefiles.VideoData.VideoSubFolderActivity


class SecureFolderAdapter(
    var activity: Activity,
    private var folders: List<FolderItem>,
    var comefrom: String,
    var folderPath: String
) : RecyclerView.Adapter<SecureFolderAdapter.FolderViewHolder>() {

    class FolderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.folderName)
        val fileCount: TextView = view.findViewById(R.id.fileCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_securefolder, parent, false)
        return FolderViewHolder(view)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folder = folders[position]
        holder.tvName.text = folder.name
        holder.fileCount.text = folder.fileCount.toString() + " Items"

        Log.e("folderPath", "folderPath Adapter :: $folderPath")
        Log.e("folderPath", "comefrom Adapter11 :: $comefrom")

        holder.itemView.setOnClickListener {

            when {
                comefrom.equals("FolderAudio") -> {
                    val intent = Intent(activity, AudioSubFolderActivity::class.java)
                        .putExtra("folderName", comefrom)
                        .putExtra("folderPath", folder.path)
                    activity.startActivity(intent)

                }

                comefrom.equals("ALLFile") -> {
                    val intent = Intent(activity, SecureSubFolderActivity::class.java)
                        .putExtra("folderPath", folder.path)
                        .putExtra("folderName", comefrom)
                    activity.startActivity(intent)

                }

                comefrom.equals("FolderDocument") -> {
                    val intent = Intent(activity, DocSubFolderActivity::class.java)
                        .putExtra("folderPath", folder.path)
                        .putExtra("folderName", comefrom)
                    activity.startActivity(intent)
                }

                comefrom.equals("FolderImage") -> {
                    val intent = Intent(activity, IMGSubFolderActivity::class.java)
                        .putExtra("folderPath", folder.path)
                        .putExtra("folderName", comefrom)
                    activity.startActivity(intent)
                }

                comefrom.equals("FolderVideo") -> {
                    val intent = Intent(activity, VideoSubFolderActivity::class.java)
                        .putExtra("folderPath", folder.path)
                        .putExtra("folderName", comefrom)
                    activity.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount() = folders.size

    fun updateList(newFolders: List<FolderItem>) {
        folders = newFolders
        notifyDataSetChanged()
    }
}
