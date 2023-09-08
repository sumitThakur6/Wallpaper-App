package com.example.wallpaperapp.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wallpaperapp.models.Converters
import com.example.wallpaperapp.models.Images

@Database(entities = [Images::class], version = 1)
@TypeConverters(Converters::class)
abstract class ImageDatabase : RoomDatabase() {

    abstract fun getImageDao() : ImageDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ImageDatabase? = null

        fun getDatabase(context: Context): ImageDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ImageDatabase::class.java,
                    "image_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}