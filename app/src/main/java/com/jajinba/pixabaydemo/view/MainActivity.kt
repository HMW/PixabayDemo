package com.jajinba.pixabaydemo.view

import android.app.SearchManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.google.android.material.tabs.TabLayoutMediator
import com.jajinba.pixabaydemo.R
import com.jajinba.pixabaydemo.adapter.ViewPagerAdapter
import com.jajinba.pixabaydemo.databinding.ActivityMainBinding
import com.jajinba.pixabaydemo.utils.InjectUtils
import com.jajinba.pixabaydemo.view.fragment.ImageGridFragment
import com.jajinba.pixabaydemo.view.fragment.ImageListFragment
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

  companion object {
    private val TAG = MainActivity::class.java.simpleName
  }

  private lateinit var binding: ActivityMainBinding

  private val imageDataSource = InjectUtils.providerImagesDataSource()
  private val isLoading: LiveData<Boolean>
    get() = imageDataSource.isLoading
  private val isLoadSuccess: LiveData<Boolean>
    get() = imageDataSource.isLoadSuccess

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    // init view
    initView()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.options_menu, menu)

    // Associate searchable configuration with the SearchView
    val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
    val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
    searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

    // show search complete button
    searchView.isSubmitButtonEnabled = true
    searchView.setOnQueryTextListener(this)
    return true
  }

  private fun initView() {
    // TODO error handling, e.g., no network
    val fragmentList: MutableList<Fragment> = ArrayList()
    fragmentList.add(ImageListFragment.newInstance(imageDataSource))
    fragmentList.add(ImageGridFragment.newInstance(imageDataSource))

    // setup ViewPager
    binding.viewPager.adapter = ViewPagerAdapter(
      supportFragmentManager,
      fragmentList,
      lifecycle
    )

    isLoading.observe(this) {
      binding.progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
    }
    isLoadSuccess.observe(this) {
      if (it.not()) {
        showErrorDialog(imageDataSource.errorMsg)
      }
    }

    // setup TabLayout
    TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
      tab.text = getText(if (position == 0) R.string.tab_title_list else R.string.tab_title_grid)
    }.attach()
  }

  private fun showErrorDialog(errorMsg: String?) {
    if (this.isFinishing.not()) {
      // TODO should create a custom dialog helper class
      AlertDialog.Builder(this)
        .setMessage(errorMsg)
        .setPositiveButton(R.string.ok) { _, _ ->
          // do nothing
        }
        .create()
        .show()
    }
  }

  override fun onQueryTextSubmit(query: String): Boolean {
    Log.d(TAG, "Submit search query")
    imageDataSource.loadImages(query)
    return true
  }

  override fun onQueryTextChange(newText: String?): Boolean {
    // TODO
    return false
  }
}
