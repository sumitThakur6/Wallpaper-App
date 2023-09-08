package com.example.wallpaperapp.viewModel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wallpaperapp.models.ImageResponse
import com.example.wallpaperapp.models.Images
import com.example.wallpaperapp.models.SearchImageResponse
import com.example.wallpaperapp.repository.WallpaperRepository
import com.example.wallpaperapp.utils.Network
import com.example.wallpaperapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.lang.Exception

class WallpaperViewModel(private val application: Application, private val repository: WallpaperRepository) : ViewModel(){
    val imageList : MutableLiveData<Resource<MutableList<ImageResponse>>> = MutableLiveData()
    private var pageNumber = 1
    private var imageResponse : MutableList<ImageResponse>? = null



    val searchImageList : MutableLiveData<SearchImageResponse> = MutableLiveData()
    private var searchPage = 1

    val topicImageList : MutableLiveData<Resource<ArrayList<ImageResponse>>> = MutableLiveData()
    private var topicResponse : ArrayList<ImageResponse>? = null
    private var topicPageNumber : Int = 1

    val imageByTopicList : MutableLiveData<Resource<ArrayList<ImageResponse>>> = MutableLiveData()
    private val imageByTopicPageNumber = 1




    init {
        getImages()
    }



    fun getImages() {
       safeGetImages()
    }
    private fun safeGetImages()  = viewModelScope.launch{

        try{
            if(Network.hasInternetConnection(application)){
                imageList.postValue(Resource.Loading())
                val response = repository.getImages(pageNumber)
                imageList.postValue(handleImageResponse(response))
            }
            else{
                imageList.postValue(Resource.Error(message = "No Internet Connection"))
            }
        } catch (e : Exception){
            when(e) {
                is IOException -> imageList.postValue(Resource.Error(message = "Network Failure"))
                else -> imageList.postValue(Resource.Error(message = "Slow Internet"))
            }
        }

    }
    private fun handleImageResponse(
        response: Response<MutableList<ImageResponse>>
    ): Resource<MutableList<ImageResponse>> {
        if(response.isSuccessful){
            response.body()?.let{
                pageNumber++
                if(imageResponse == null){
                    imageResponse = it
                }else{
                    val oldImages = imageResponse
                    val newImages = it
                    oldImages?.addAll(newImages)
                }
                return Resource.Success(imageResponse ?: it)
            }
        }
        return Resource.Error(message = response.message())
    }

    fun searchImages(query : String) = viewModelScope.launch {
        val response = repository.searchImages(searchPage, query)
        if(response.isSuccessful && response.body() != null){
            searchImageList.postValue(response.body())
        }
    }


    fun getImagesTopics() {
       safeCallTopicImages()
    }
    private fun safeCallTopicImages() = viewModelScope.launch {
        try {
            if(Network.hasInternetConnection(application)){
                topicImageList.postValue(Resource.Loading())
                val response = repository.getImagesTopics(topicPageNumber)
                if(response.isSuccessful && response.body() != null){
                    topicImageList.postValue(handleImagesTopics(response))
                }
            }
            else{
                topicImageList.postValue(Resource.Error(message = "Not Internet Connection"))
            }
        } catch (e : Exception){
            when(e) {
                is IOException ->   topicImageList.postValue(Resource.Error(message = "Network Failure"))
                else ->   topicImageList.postValue(Resource.Error(message = "Slow Internet"))
            }
        }
    }
    private fun handleImagesTopics(response: Response<List<ImageResponse>>): Resource<ArrayList<ImageResponse>> {
       if (response.isSuccessful){
           response.body()?.let{
               topicPageNumber++
               if(topicResponse == null){
                   topicResponse = it as ArrayList<ImageResponse>
               }
               else{
                   val newImages = it
                   topicResponse?.addAll(newImages)
               }

               return Resource.Success(topicResponse ?: it as ArrayList<ImageResponse>)
           }
       }

        return Resource.Error(message = response.message())
    }


    fun getImagesByTopics( topicId: String){
        safeGetImagesByTopics(topicId)
    }
    private fun safeGetImagesByTopics(topicId: String) = viewModelScope.launch {
        try {
            if(Network.hasInternetConnection(application)){
                imageByTopicList.postValue(Resource.Loading())
                val response = repository.getImagesByTopics(topicId, imageByTopicPageNumber)
                imageByTopicList.postValue(handleImageByTopics(response))

            }else{
                imageByTopicList.postValue(Resource.Error(message = "No Internet Connection"))
            }
        }  catch (e : Exception){
            when(e) {
                is IOException -> imageByTopicList.postValue(Resource.Error(message = "Network Failed"))
                else -> imageByTopicList.postValue(Resource.Error(message = "Slow Internet"))
            }
        }
    }
    private fun handleImageByTopics(response: Response<MutableList<ImageResponse>>): Resource<ArrayList<ImageResponse>> {
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it as ArrayList<ImageResponse>)
            }
        }
        return Resource.Error(message = response.message())
    }


    fun getSavedImages() = repository.getSavedImages()

    fun insertImages(image: Images) = viewModelScope.launch {
        repository.insertImage(image)
    }


}


class ViewModelFactory(private val application: Application, private val repository: WallpaperRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WallpaperViewModel(application, repository) as T
    }
}