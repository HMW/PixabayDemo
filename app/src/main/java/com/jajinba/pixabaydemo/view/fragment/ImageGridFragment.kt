package com.jajinba.pixabaydemo.view.fragment

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class ImageGridFragment : ListFragment() {

  companion object {
    private const val SPAN_COUNT = 2
    fun newInstance(): ImageGridFragment {
      return ImageGridFragment()
    }
  }

  override fun getLayoutManager(): RecyclerView.LayoutManager {
    return StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
  }

  override fun onFailed(errorMsg: Int) {
    // TODO
  }

}
