package com.example.wallpaperapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallpaperapp.R
import com.example.wallpaperapp.models.ImageResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WallPaperAdapter(private val context : Context, private val listener : OnItemClick) : ListAdapter<ImageResponse, WallPaperAdapter.WallpaperViewHolder>(DiffUtilCallBack()) {

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_open_enim) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.rotate_close_enim) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.from_bottom_enim) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(context, R.anim.to_bottom_enim) }
    private var clicked = false

    inner class WallpaperViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val image : ImageView = itemView.findViewById(R.id.ivWallpaper)
        val btnDownload : FloatingActionButton = itemView.findViewById(R.id.btnDownload)
        val btnExpand :  FloatingActionButton = itemView.findViewById(R.id.btnExpand)
        val btnSave :  FloatingActionButton = itemView.findViewById(R.id.btnSave)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        return WallpaperViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_wallpaper, parent, false))
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val imageItems = getItem(position)

            val url = imageItems.urls.regular
            Glide.with(context).load(url).into(holder.image)


        holder.btnExpand.setOnClickListener{
            onExpandButtonClick(clicked, holder)
            setAnimation(clicked, holder)
            setClickble(clicked, holder)
            clicked = !clicked
        }

        holder.btnDownload.setOnClickListener{
            listener.OnDownloadBtnClick(url)
        }

        holder.btnSave.setOnClickListener{
            listener.OnSaveBtnClick(url)
        }
    }




    private fun onExpandButtonClick(clicked: Boolean, holder: WallpaperViewHolder){
        if(!clicked){
            holder.btnDownload.visibility = View.VISIBLE
            holder.btnSave.visibility = View.VISIBLE
        }
        else{
            holder.btnDownload.visibility = View.INVISIBLE
            holder.btnSave.visibility = View.INVISIBLE
        }
    }

    private fun setAnimation(clicked: Boolean, holder: WallpaperViewHolder) {
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

    private fun setClickble(clicked: Boolean, holder: WallPaperAdapter.WallpaperViewHolder) {
        if(!clicked){
            holder.btnSave.isClickable = true
            holder.btnDownload.isClickable = true
        }
        else{
            holder.btnSave.isClickable = false
            holder.btnDownload.isClickable = false
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<ImageResponse>(){
        override fun areItemsTheSame(oldItem: ImageResponse, newItem: ImageResponse): Boolean {
           return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ImageResponse, newItem: ImageResponse): Boolean {
                return newItem == oldItem
        }
    }

}

interface OnItemClick{
    fun OnDownloadBtnClick(url : String)
    fun OnSaveBtnClick(url : String)
}
