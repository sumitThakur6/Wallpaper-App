package com.example.wallpaperapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.wallpaperapp.R
import com.example.wallpaperapp.adapters.ViewPagerAdapter
import com.example.wallpaperapp.dao.ImageDatabase
import com.example.wallpaperapp.databinding.ActivityMainBinding
import com.example.wallpaperapp.repository.WallpaperRepository
import com.example.wallpaperapp.viewModel.ViewModelFactory
import com.example.wallpaperapp.viewModel.WallpaperViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewPager2 : ViewPager2
    private lateinit var tabLayout: TabLayout

     lateinit var viewModel : WallpaperViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.customToolbar)

       val repository = WallpaperRepository(ImageDatabase.getDatabase(this))
        val viewModelFactory = ViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[WallpaperViewModel::class.java]

        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager2
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = viewPagerAdapter


        val tabNames = arrayOf("Home", "Explore", "Saved")
        TabLayoutMediator(tabLayout, viewPager2){ tab, position ->
          tab.text = tabNames[position]
        }.attach()

//        setupTabLayout()

    }

    private fun setupTabLayout() {

        viewPager2.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("Home"))
        tabLayout.addTab(tabLayout.newTab().setText("Explore"))
        tabLayout.addTab(tabLayout.newTab().setText("Saved"))


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search ->{
               val intent = Intent(this, SearchImageActivity::class.java)
               startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}