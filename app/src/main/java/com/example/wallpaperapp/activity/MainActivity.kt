package com.example.wallpaperapp.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        getPermission()

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

    private fun getPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
    }

}