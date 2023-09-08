package com.example.wallpaperapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wallpaperapp.activity.MainActivity
import com.example.wallpaperapp.adapters.ImageDBAdapter
import com.example.wallpaperapp.databinding.FragmentSavedBinding
import com.example.wallpaperapp.viewModel.WallpaperViewModel


class SavedFragment : Fragment() {

    private lateinit var binding : FragmentSavedBinding

    private lateinit var viewModel : WallpaperViewModel
    private lateinit var mAdapter : ImageDBAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentSavedBinding.inflate(inflater, container, false)

        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.getSavedImages().observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        mAdapter = ImageDBAdapter()
        binding.rvSavedImage.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mAdapter
        }
    }
}