package com.example.wallpaperapp.activity

import android.app.DownloadManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wallpaperapp.R
import com.example.wallpaperapp.adapters.OnItemClick
import com.example.wallpaperapp.adapters.WallPaperAdapter
import com.example.wallpaperapp.dao.ImageDatabase
import com.example.wallpaperapp.databinding.ActivityImageTopicBinding
import com.example.wallpaperapp.models.Images
import com.example.wallpaperapp.repository.WallpaperRepository
import com.example.wallpaperapp.utils.Network
import com.example.wallpaperapp.utils.Resource
import com.example.wallpaperapp.viewModel.ViewModelFactory
import com.example.wallpaperapp.viewModel.WallpaperViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.File

class ImageTopicActivity : AppCompatActivity(), OnItemClick {

    private lateinit var binding : ActivityImageTopicBinding
    private lateinit var viewModel : WallpaperViewModel
    private lateinit var mAdapter : WallPaperAdapter

    private var permissionValue = 0
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        permissionValue = if (it){
            1
        }
        else{
            0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.customToolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)

        val intent = intent
        val topicId = intent.getStringExtra("topicId")


        val repository = WallpaperRepository(ImageDatabase.getDatabase(this))
        val viewModelFactory = ViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[WallpaperViewModel::class.java]

        setupRecyclerView()

        if (topicId != null) {
            viewModel.getImagesByTopics(topicId)
        }


        viewModel.imageByTopicList.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    mAdapter.submitList(response.data)
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    private fun setupRecyclerView() {
        mAdapter = WallPaperAdapter(this, this)
        binding.rvImageTopic.apply {
            layoutManager = GridLayoutManager(this@ImageTopicActivity, 2)
            adapter = mAdapter
        }
    }

    override fun OnDownloadBtnClick(url: String) {
        requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permissionValue == 1)
            downloadImageFromUrl(url)
    }

    override fun OnSaveBtnClick(url: String) {
        lifecycleScope.launch {
            val imageBitmap = Network.getBitmap(this@ImageTopicActivity, url)
            val image = Images(image = imageBitmap)
            viewModel.insertImages(image)
        }
        Snackbar.make(binding.root, "Image saved successfully", Snackbar.LENGTH_SHORT).show()
    }


    private fun downloadImageFromUrl(url: String) {
        try{
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val imageLink = Uri.parse(url)
            val request = DownloadManager.Request(imageLink)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setMimeType("image/jpeg")
                .setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle("wallpaper")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator+"wallpaper"+".jpg")
            downloadManager.enqueue(request)
            Toast.makeText(this, "Image is Downloading", Toast.LENGTH_SHORT).show()
        }
        catch (e : Exception){
            Toast.makeText(this, "Download Failed", Toast.LENGTH_SHORT).show()
        }
    }
}