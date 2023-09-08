package com.example.wallpaperapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.wallpaperapp.R
import com.example.wallpaperapp.models.Images
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ImageDBAdapter : ListAdapter<Images, ImageDBAdapter.ViewHolder>(DiffUtilCallBack()) {
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.ivWallpaper)
        val button : FloatingActionButton = itemView.findViewById(R.id.btnExpand)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_wallpaper, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.button.visibility = View.INVISIBLE
        holder.image.load(item.image)
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<Images>(){
        override fun areItemsTheSame(oldItem: Images, newItem: Images): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: Images, newItem: Images): Boolean {
            return oldItem.id == newItem.id
        }
    }


}