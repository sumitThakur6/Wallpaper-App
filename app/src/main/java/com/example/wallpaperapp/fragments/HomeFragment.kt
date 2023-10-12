package com.example.wallpaperapp.fragments

import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpaperapp.activity.MainActivity
import com.example.wallpaperapp.adapters.OnItemClick
import com.example.wallpaperapp.adapters.WallPaperAdapter
import com.example.wallpaperapp.databinding.FragmentHomeBinding
import com.example.wallpaperapp.models.Images
import com.example.wallpaperapp.utils.Network
import com.example.wallpaperapp.utils.Resource
import com.example.wallpaperapp.viewModel.WallpaperViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception


class HomeFragment : Fragment(), OnItemClick {

    private lateinit var mAdapter : WallPaperAdapter
    private lateinit var binding : FragmentHomeBinding
    private lateinit var viewModel: WallpaperViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding =  FragmentHomeBinding.inflate(inflater, container, false)
        setupRecyclerView()

        viewModel = (activity as MainActivity).viewModel

        viewModel.imageList.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    response.data?.let {
                        mAdapter.submitList(it)
                    }
                }

                is Resource.Loading -> {
                   binding.progressBar.visibility = View.VISIBLE
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
            }
        })


        return binding.root
    }

    private fun setupRecyclerView() {
        mAdapter = WallPaperAdapter(requireContext(), this)
        binding.rv.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = mAdapter
            addOnScrollListener(this@HomeFragment.onScrollingListener)
        }
    }

    override fun OnDownloadBtnClick(url: String) {
            downloadImageFromUrl(url)
    }

    override fun OnSaveBtnClick(url: String) {
        lifecycleScope.launch {
            val imageBitmap = Network.getBitmap(requireContext(), url)
            val image = Images(image = imageBitmap)
            viewModel.insertImages(image)
        }
        Snackbar.make(binding.root, "Image saved successfully", Snackbar.LENGTH_SHORT).show()
    }

    private fun downloadImageFromUrl(url: String) {
        try{
            val downloadManager = requireContext().getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val imageLink = Uri.parse(url)
            val request = DownloadManager.Request(imageLink)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setMimeType("image/jpeg")
                .setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle("wallpaper")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.separator+"wallpaper"+".jpg")
            downloadManager.enqueue(request)
            Toast.makeText(requireContext(), "Image is Downloading", Toast.LENGTH_SHORT).show()
        }
        catch (e : Exception){
            Toast.makeText(requireContext(), "Download Failed", Toast.LENGTH_SHORT).show()
        }
    }


    var isScrolling = false
    private val onScrollingListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                isScrolling = true
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager  as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val totalItem = layoutManager.itemCount
            val visibleItem = layoutManager.childCount

            if(isScrolling && (visibleItem + firstVisibleItemPosition) >= totalItem){
                viewModel.getImages()
                isScrolling = false
            }
        }
    }


}