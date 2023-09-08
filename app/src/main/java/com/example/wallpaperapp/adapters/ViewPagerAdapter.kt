package com.example.wallpaperapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.wallpaperapp.fragments.ExploreFragment
import com.example.wallpaperapp.fragments.HomeFragment
import com.example.wallpaperapp.fragments.SavedFragment

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifeCycle : Lifecycle
) : FragmentStateAdapter(fragmentManager, lifeCycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return when(position){
            0 -> HomeFragment()
            1 -> ExploreFragment()
            else -> SavedFragment()
       }
    }

}