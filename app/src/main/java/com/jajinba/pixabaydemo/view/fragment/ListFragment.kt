package com.jajinba.pixabaydemo.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jajinba.pixabaydemo.R
import com.jajinba.pixabaydemo.adapter.ImageListAdapter
import com.jajinba.pixabaydemo.contract.ListContract
import com.jajinba.pixabaydemo.databinding.FragmentListBinding
import com.jajinba.pixabaydemo.model.ImageManager
import com.jajinba.pixabaydemo.model.ImagesSearchDataSource
import com.jajinba.pixabaydemo.model.PixabayImageObject
import com.jajinba.pixabaydemo.utils.ArrayUtils
import java.util.*

abstract class ListFragment(private val imageSearchDataSource: ImagesSearchDataSource)
  : BaseFragment(R.layout.fragment_list), ListContract.View {

  companion object {
    private val TAG = ListFragment::class.java.simpleName
    private const val BUNDLE_IMAGE_KEY: String = "bundle.image.key"
  }

  private var binding: FragmentListBinding? = null
  private lateinit var adapter: ImageListAdapter
  // cache keyword to restore image list after screen rotated
  private var mCurrentKeyword: String? = null

  private val adapterCallback = object: ImageListAdapter.Callback {
    override fun loadMore() {
      mCurrentKeyword?.let {
//        imageSearchDataSource.loadImages(it, imageSearchDataSource.page.value ?: 1)
      }
    }
  }

  protected abstract fun getLayoutManager(): RecyclerView.LayoutManager?
  abstract fun setImageList(keyword: String, imageList: MutableList<PixabayImageObject>)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    adapter = ImageListAdapter(context, adapterCallback)
    binding = FragmentListBinding.bind(view)
    binding?.recyclerView?.let { recyclerView ->
      recyclerView.adapter = adapter
      recyclerView.layoutManager = getLayoutManager()
      recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
          super.onScrolled(recyclerView, dx, dy)
          if (adapter.itemCount > 0 && recyclerView.canScrollVertically(1).not()) {
            mCurrentKeyword?.let {
              Log.d(TAG, "onScrolled, page = ${imageSearchDataSource.page.value}")
              imageSearchDataSource.loadImages(
                it,
                (imageSearchDataSource.page.value?.plus(1)) ?: 1)
            }
          }
        }
      })
    }

    // restore state before rotation
    if (savedInstanceState != null) {
      // get previous keyword
      mCurrentKeyword = savedInstanceState.getString(BUNDLE_IMAGE_KEY)

      // clear instance state
      savedInstanceState.clear()

      // restore ui
      mCurrentKeyword?.let {
        searchFinished(it, ImageManager.getInstance().getImageListWithKeyword(it))
      }
    }

    imageSearchDataSource.imageList.observe(viewLifecycleOwner) { imageList ->
      Log.d(TAG, "fragment observer - image list updated")
      imageSearchDataSource.keyword.value?.let { keyword ->
        listUpdated(keyword, imageList)
      }
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putString(BUNDLE_IMAGE_KEY, mCurrentKeyword)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }

  override fun searchFinished(keyword: String, imageList: MutableList<PixabayImageObject>) {
    Log.d(TAG, "Receive search finished callback")

    // FIXME check is attached?
//    listUpdated(keyword, imageList)
  }

  private fun listUpdated(keyword: String, imageList: MutableList<PixabayImageObject>) {
    Log.d(TAG, "Receive search finished callback")

    // FIXME check is attached?
    if (isFragmentValid().not()) {
      return
    }
    Log.d(TAG, ArrayUtils.getLengthSafe(imageList).toString() + " image received")
//    val isKeywordChanged = keyword.equals(mCurrentKeyword)
    mCurrentKeyword = keyword
    if (TextUtils.isEmpty(keyword)) {
      resetUi()
    } else {
      updateUi(imageList)
    }
  }

  private fun resetUi() {
    adapter.apply {
      updateList(ArrayList())
      notifyDataSetChanged()
    }
  }

  private fun updateUi(imageList: MutableList<PixabayImageObject>) {
    binding?.apply {
      val isListEmpty = imageList.isEmpty()
      tvEmptyState.visibility = if (isListEmpty) View.VISIBLE else View.GONE
      recyclerView.visibility = if (isListEmpty) View.GONE else View.VISIBLE
    }
    if (adapter.itemCount == 1) {
      adapter.updateList(imageList)
      adapter.notifyDataSetChanged()
    } else {
      val oldImageCount = if (adapter.itemCount > 0) adapter.itemCount - 1 else adapter.itemCount
      adapter.updateList(imageList)
      binding?.recyclerView?.post {
        adapter.notifyItemRangeInserted(
          oldImageCount,
          ArrayUtils.getLengthSafe(imageList) - oldImageCount
        )
      }
    }
  }
}
