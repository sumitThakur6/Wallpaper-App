package com.example.wallpaperapp.activity

import android.app.DownloadManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wallpaperapp.R
import com.example.wallpaperapp.adapters.OnSearchItemClick
import com.example.wallpaperapp.adapters.SearchImageAdapter
import com.example.wallpaperapp.dao.ImageDatabase
import com.example.wallpaperapp.databinding.ActivitySearchImageBinding
import com.example.wallpaperapp.models.Images
import com.example.wallpaperapp.repository.WallpaperRepository
import com.example.wallpaperapp.utils.Network
import com.example.wallpaperapp.viewModel.ViewModelFactory
import com.example.wallpaperapp.viewModel.WallpaperViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.File

class SearchImageActivity : AppCompatActivity(), OnSearchItemClick {

    private lateinit var binding : ActivitySearchImageBinding
    private lateinit var viewModel : WallpaperViewModel
    private lateinit var mAdapter : SearchImageAdapter

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
        binding = ActivitySearchImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.customToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)

        val repository = WallpaperRepository(ImageDatabase.getDatabase(this))
        val viewModelFactory = ViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[WallpaperViewModel::class.java]

        setupRecyclerView()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                handleSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               return false
            }

        })
    }

    private fun handleSearch(query: String?) {
        if (query != null) {
            viewModel.searchImages(query)
            viewModel.searchImageList.observe(this, Observer {
                mAdapter.submitList(it.results)
            })
        }

    }

    private fun setupRecyclerView() {
        mAdapter = SearchImageAdapter(this, this)
        binding.rvSearchImage.apply{
            layoutManager = GridLayoutManager(context, 2)
            adapter = mAdapter
        }
    }

    override fun OnDownloadBtnClick(url: String) {
        requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if(permissionValue == 1)
            downloadImageFromUrl(url)
    }

    override fun OnSaveButtonClick(url: String) {
        lifecycleScope.launch {
            val imageBitmap = Network.getBitmap(this@SearchImageActivity, url)
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