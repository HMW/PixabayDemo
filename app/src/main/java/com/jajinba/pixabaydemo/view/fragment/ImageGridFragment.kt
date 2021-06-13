package com.jajinba.pixabaydemo.view.fragment

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jajinba.pixabaydemo.model.ImagesSearchDataSource
import com.jajinba.pixabaydemo.model.PixabayImageObject

class ImageGridFragment(imageSearchDataSource: ImagesSearchDataSource) : ListFragment(imageSearchDataSource) {

  companion object {
    private const val SPAN_COUNT = 2
    fun newInstance(imageSearchDataSource: ImagesSearchDataSource): ImageGridFragment {
      return ImageGridFragment(imageSearchDataSource)
    }
  }

  override fun getLayoutManager(): RecyclerView.LayoutManager {
    return StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
  }

  override fun setImageList(keyword: String, imageList: MutableList<PixabayImageObject>) {
    searchFinished(keyword, imageList)
  }

  override fun onFailed(errorMsg: Int) {
    // TODO
  }

}
