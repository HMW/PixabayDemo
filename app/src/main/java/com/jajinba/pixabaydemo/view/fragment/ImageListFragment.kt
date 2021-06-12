package com.jajinba.pixabaydemo.view.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ImageListFragment : ListFragment() {

  companion object {
    fun newInstance(): ImageListFragment {
      return ImageListFragment()
    }
  }

  override fun getLayoutManager(): RecyclerView.LayoutManager {
    return LinearLayoutManager(context)
  }

  override fun onFailed(errorMsg: Int) {
    // TODO
  }

}
