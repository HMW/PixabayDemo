package com.jajinba.pixabaydemo.view.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jajinba.pixabaydemo.Constants
import com.jajinba.pixabaydemo.R
import com.jajinba.pixabaydemo.adapter.ImageListAdapter
import com.jajinba.pixabaydemo.contract.ListContract
import com.jajinba.pixabaydemo.databinding.FragmentListBinding
import com.jajinba.pixabaydemo.model.ImageManager
import com.jajinba.pixabaydemo.model.PixabayImageObject
import com.jajinba.pixabaydemo.presenter.ListPresenter
import com.jajinba.pixabaydemo.utils.ArrayUtils
import java.util.*

abstract class ListFragment : BaseFragment(R.layout.fragment_list), ListContract.View {

  companion object {
    private val TAG = ListFragment::class.java.simpleName
    private const val BUNDLE_IMAGE_KEY: String = "bundle.image.key"
  }

  private var binding: FragmentListBinding? = null
  private lateinit var mPresenter: ListPresenter
  private lateinit var adapter: ImageListAdapter
  // cache keyword to restore image list after screen rotated
  private var mCurrentKeyword: String? = null

  private val adapterCallback = object: ImageListAdapter.Callback {
    override fun loadMore() {
      mCurrentKeyword?.let { mPresenter.loadMore(it) }
    }
  }

  protected abstract fun getLayoutManager(): RecyclerView.LayoutManager?

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    adapter = ImageListAdapter(context, adapterCallback)
    binding = FragmentListBinding.bind(view)
    binding?.recyclerView?.let {
      it.adapter = adapter
      it.layoutManager = getLayoutManager()
    }
    mPresenter = ListPresenter(this)

    // restore state before rotation
    if (savedInstanceState != null) {
      // get previous keyword
      mCurrentKeyword = savedInstanceState.getString(BUNDLE_IMAGE_KEY)

      // clear instance state
      savedInstanceState.clear()

      // restore ui
      mCurrentKeyword?.let {
        searchFinished(it, mPresenter.getImageList(it))
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
    if (isFragmentValid().not()) {
      return
    }
    Log.d(TAG, ArrayUtils.getLengthSafe(imageList).toString() + " image received")
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
      tvEmptyState.visibility = if (imageList.isEmpty()) View.VISIBLE else View.GONE
      recyclerView.visibility = if (imageList.isNotEmpty()) View.VISIBLE else View.GONE
    }

    @ImageManager.Operation val lastOperation = mPresenter.getLastOperation()
    Log.d(TAG, "hm] update ui state with operation: $lastOperation, image count: ${imageList.size}")
    Log.d(TAG, "hm] empty view visible: ${(binding?.tvEmptyState?.visibility == View.VISIBLE)}")
    Log.d(TAG, "hm] recycler view visible: ${(binding?.recyclerView?.visibility == View.VISIBLE)}")

    adapter.updateList(imageList)
    if (ImageManager.NEW_SEARCH == lastOperation) {
      adapter.notifyDataSetChanged()
    } else if (ImageManager.LOAD_MORE == lastOperation) {
      val oldImageCount =
        if (ArrayUtils.getLengthSafe(imageList) % Constants.IMAGE_PER_PAGE == 0) ArrayUtils.getLengthSafe(
          imageList
        ) - Constants.IMAGE_PER_PAGE else ArrayUtils.getLengthSafe(imageList) % Constants.IMAGE_PER_PAGE
      adapter.notifyItemRangeInserted(
        oldImageCount + 1,
        ArrayUtils.getLengthSafe(imageList) - oldImageCount
      )
    }
  }
}
