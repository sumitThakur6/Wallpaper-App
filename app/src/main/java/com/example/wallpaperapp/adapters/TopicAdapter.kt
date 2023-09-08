package com.example.wallpaperapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallpaperapp.R
import com.example.wallpaperapp.activity.ImageTopicActivity
import com.example.wallpaperapp.models.ImageResponse


class TopicAdapter : ListAdapter<ImageResponse, TopicAdapter.ViewHolder>(WallPaperAdapter.DiffUtilCallBack()) {

    inner class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val topicImage : ImageView = itemView.findViewById(R.id.topicImage)
        val topicTitle : TextView = itemView.findViewById(R.id.topicTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_topic_images, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.topicTitle.text = item.title
        val url = item.cover_photo.urls.regular
        Glide.with(holder.itemView.context).load(url).into(holder.topicImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ImageTopicActivity::class.java)
            intent.putExtra("topicId", item.id)
            holder.itemView.context.startActivity(intent)
        }
    }

}

