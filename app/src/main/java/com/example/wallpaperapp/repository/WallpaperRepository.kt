package com.example.wallpaperapp.repository

import com.example.wallpaperapp.apis.RetrofitHelper
import com.example.wallpaperapp.dao.ImageDatabase
import com.example.wallpaperapp.models.Images

class WallpaperRepository (val db : ImageDatabase){

    suspend fun getImages(pageNumber : Int) = RetrofitHelper.apiInterface.getPhotos(pageNumber = pageNumber)

    suspend fun getImagesTopics(pageNumber : Int) = RetrofitHelper.apiInterface.getImagesTopics(pageNumber = pageNumber)

    suspend fun getImagesByTopics(topic : String, page : Int) = RetrofitHelper.apiInterface.getImagesByTopics(topicId = topic, pageNumber = page)

    suspend fun searchImages(pageNumber: Int, query : String) = RetrofitHelper.apiInterface.searchPhotos(pageNumber = pageNumber, query = query)


    fun getSavedImages() = db.getImageDao().getAllImages()

    suspend fun insertImage(image : Images) = db.getImageDao().insert(image)

}