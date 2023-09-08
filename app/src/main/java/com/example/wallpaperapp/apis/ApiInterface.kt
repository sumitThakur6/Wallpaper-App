package com.example.wallpaperapp.apis

import com.example.wallpaperapp.models.ImageResponse
import com.example.wallpaperapp.models.SearchImageResponse
import com.example.wallpaperapp.utils.Constants.CLIENT_ID
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("photos")
    suspend fun getPhotos(
        @Query("client_id") apiKey : String = CLIENT_ID,
        @Query("page") pageNumber : Int = 1,
        @Query("per_page") perPage : Int = 30
    ) : Response<MutableList<ImageResponse>>

    @GET("topics")
    suspend fun getImagesTopics(
        @Query("client_id") apiKey : String = CLIENT_ID,
        @Query("page") pageNumber : Int = 1
    ) : Response<List<ImageResponse>>

    @GET("topics/{topic_id}/photos")
    suspend fun getImagesByTopics(
        @Path("topic_id") topicId : String,
        @Query("client_id") apiKey : String = CLIENT_ID,
        @Query("page") pageNumber : Int = 1
    ) : Response<MutableList<ImageResponse>>



    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("client_id") apiKey : String = CLIENT_ID,
        @Query("page") pageNumber : Int = 1,
        @Query("query") query : String,
        @Query("per_page") imagePerPage : Int = 30
    ) : Response<SearchImageResponse>

}