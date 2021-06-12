package com.jajinba.pixabaydemo.view

import android.app.SearchManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jajinba.pixabaydemo.MainApplication
import com.jajinba.pixabaydemo.R
import com.jajinba.pixabaydemo.adapter.ViewPagerAdapter
import com.jajinba.pixabaydemo.contract.MainActivityContract
import com.jajinba.pixabaydemo.databinding.ActivityMainBinding
import com.jajinba.pixabaydemo.presenter.MainActivityPresenter
import com.jajinba.pixabaydemo.view.fragment.ImageGridFragment
import com.jajinba.pixabaydemo.view.fragment.ImageListFragment
import java.util.*

class MainActivity : AppCompatActivity(), MainActivityContract.View,
  SearchView.OnQueryTextListener {

  companion object {
    private val TAG = MainActivity::class.java.simpleName
  }

  private lateinit var binding: ActivityMainBinding
  private var mPresenter: MainActivityContract.Presenter? = null
  private var isSearching = false

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
    mPresenter = MainActivityPresenter(this)
    val fragmentList: MutableList<Fragment> = ArrayList()
    fragmentList.add(ImageListFragment.newInstance())
    fragmentList.add(ImageGridFragment.newInstance())

    // setup ViewPager
    binding.viewPager.adapter = ViewPagerAdapter(
      supportFragmentManager,
      fragmentList,
      lifecycle
    )

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

  override fun searchStart() {
    isSearching = true
    binding.progressBar.visibility = View.VISIBLE
  }

  override fun searchFinished(isSuccess: Boolean, @StringRes errorMsg: Int) {
    Log.d(TAG, "searchFinished: $isSuccess")
    isSearching = false
    binding.progressBar.visibility = View.INVISIBLE
    if (isSuccess.not()) {
      showErrorDialog(
        MainApplication.getInstance()?.getString(errorMsg)
      )
    }
  }

  override fun onQueryTextSubmit(query: String): Boolean {
    if (isSearching) {
      return true
    }
    Log.d(TAG, "Submit search query")
    mPresenter?.search(query)
    return true
  }

  override fun onQueryTextChange(newText: String?): Boolean {
    // TODO
    return false
  }
}
