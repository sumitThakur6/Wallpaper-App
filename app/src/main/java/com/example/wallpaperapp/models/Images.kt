package com.example.wallpaperapp.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class Images(
    @PrimaryKey(autoGenerate = true)
    var id : Long  = 0,
    val image : Bitmap,
)