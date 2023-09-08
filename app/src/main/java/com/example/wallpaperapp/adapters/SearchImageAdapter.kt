package com.example.wallpaperapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallpaperapp.R
import com.example.wallpaperapp.models.Result
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SearchImageAdapter(val context : Context, val listener : OnSearchItemClick) : ListAdapter<Result, SearchImageAdapter.ViewHolder>(DiffUtilCallBack()) {

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_enim) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_enim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_enim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_enim) }
    private var clicked = false

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.ivWallpaper)
        val btnDownload : FloatingActionButton = itemView.findViewById(R.id.btnDownload)
        val btnExpand : FloatingActionButton = itemView.findViewById(R.id.btnExpand)
        val btnSave : FloatingActionButton = itemView.findViewById(R.id.btnSave)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return (ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_wallpaper, parent, false)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val url = item.urls

        Glide.with(holder.itemView.context).load(url.regular).into(holder.image)

        holder.btnDownload.setOnClickListener{
            listener.OnDownloadBtnClick(url.regular)
        }

        holder.btnSave.setOnClickListener{
            listener.OnSaveButtonClick(url.regular)
        }


        holder.btnExpand.setOnClickListener{
            onExpandButtonClick(clicked, holder)
            setAnimation(clicked, holder)
            setClickble(clicked, holder)
            clicked = !clicked
        }
    }

    private fun onExpandButtonClick(clicked: Boolean, holder : SearchImageAdapter.ViewHolder){
        if(!clicked){
            holder.btnDownload.visibility = View.VISIBLE
            holder.btnSave.visibility = View.VISIBLE
        }
        else{
            holder.btnDownload.visibility = View.INVISIBLE
            holder.btnSave.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean, holder : SearchImageAdapter.ViewHolder) {
        if(!clicked){
            holder.btnSave.startAnimation(fromBottom)
            holder.btnDownload.startAnimation(fromBottom)
            holder.btnExpand.startAnimation(rotateOpen)
        }
        else{
            holder.btnSave.startAnimation(toBottom)
            holder.btnDownload.startAnimation(toBottom)
            holder.btnExpand.startAnimation(rotateClose)
        }
    }

    private fun setClickble(clicked: Boolean,  holder : SearchImageAdapter.ViewHolder) {
        if(!clicked){
            holder.btnSave.isClickable = true
            holder.btnDownload.isClickable = true
        }
        else{
            holder.btnSave.isClickable = false
            holder.btnDownload.isClickable = false
        }
    }


    class DiffUtilCallBack :  DiffUtil.ItemCallback<Result>(){
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

}

interface OnSearchItemClick{
    fun OnDownloadBtnClick(url : String)
    fun OnSaveButtonClick(url : String)
}