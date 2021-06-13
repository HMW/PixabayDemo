package com.jajinba.pixabaydemo.view.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jajinba.pixabaydemo.model.ImagesSearchDataSource
import com.jajinba.pixabaydemo.model.PixabayImageObject

class ImageListFragment(imageSearchDataSource: ImagesSearchDataSource) : ListFragment(imageSearchDataSource) {

  companion object {
    fun newInstance(imageSearchDataSource: ImagesSearchDataSource): ImageListFragment {
      return ImageListFragment(imageSearchDataSource)
    }
  }

  override fun getLayoutManager(): RecyclerView.LayoutManager {
    return LinearLayoutManager(context)
  }

  override fun setImageList(keyword: String, imageList: MutableList<PixabayImageObject>) {
    searchFinished(keyword, imageList)
  }

  override fun onFailed(errorMsg: Int) {
    // TODO
  }

}
