package com.example.wallpaperapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpaperapp.activity.MainActivity
import com.example.wallpaperapp.adapters.TopicAdapter
import com.example.wallpaperapp.databinding.FragmentExploreBinding
import com.example.wallpaperapp.utils.Resource
import com.example.wallpaperapp.viewModel.WallpaperViewModel


class ExploreFragment : Fragment(){
    private lateinit var binding : FragmentExploreBinding
    private lateinit var viewModel : WallpaperViewModel
    private lateinit var topicAdapter: TopicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentExploreBinding.inflate(inflater, container, false)

        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        viewModel.getImagesTopics()
        viewModel.topicImageList.observe(viewLifecycleOwner, Observer {response ->
           when(response) {
               is Resource.Success -> {
                   binding.progressBar.visibility = View.INVISIBLE
                   topicAdapter.submitList(response.data)
               }

               is Resource.Error -> {
                   binding.progressBar.visibility = View.INVISIBLE
                   Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
               }

               is Resource.Loading -> {
                   binding.progressBar.visibility = View.VISIBLE
               }
           }
        })

        return binding.root
    }

    private fun setupRecyclerView() {
        topicAdapter = TopicAdapter()
        binding.rvTopic.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = topicAdapter
            addOnScrollListener(this@ExploreFragment.scrollListener)
        }
    }

    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                isScrolling = true
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManger = recyclerView.layoutManager as LinearLayoutManager

            val firstItemPosition = layoutManger.findFirstVisibleItemPosition()
            val totalItem = layoutManger.itemCount
            val visibleItem = layoutManger.childCount

            if(isScrolling && (firstItemPosition+visibleItem )>= totalItem){
                viewModel.getImagesTopics()
                isScrolling = false
            }
        }
    }


}