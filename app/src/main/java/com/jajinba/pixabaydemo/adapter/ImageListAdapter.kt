package com.jajinba.pixabaydemo.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jajinba.pixabaydemo.Constants
import com.jajinba.pixabaydemo.R
import com.jajinba.pixabaydemo.model.PixabayImageObject
import com.jajinba.pixabaydemo.utils.ArrayUtils
import com.jajinba.pixabaydemo.view.ViewHolder

class ImageListAdapter(private val context: Context?,
                       private val callback: Callback? = null) : RecyclerView.Adapter<ViewHolder?>() {

  companion object {
    private const val VIEW_TYPE_ITEM = 5566
    private const val VIEW_TYPE_LOADING = 5567
  }

  interface Callback {
    fun loadMore()
  }

  private val mImageList = mutableListOf<PixabayImageObject>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return if (viewType == VIEW_TYPE_ITEM) ViewHolder(
      R.layout.item_image,
      parent
    ) else ViewHolder(R.layout.item_loading, parent)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    if (getItemViewType(position) == VIEW_TYPE_ITEM) {
      if (ArrayUtils.getLengthSafe(mImageList) > position) {
        context?.let {
          Glide.with(it)
            .load(mImageList[position].webformatUrl)
            .placeholder(R.drawable.placeholder) // workaround, refer glide issue 542 on Github
            .dontAnimate()
            .override(
              mImageList[position].webformatWidth,
              mImageList[position].webformatHeight
            )
            .into(holder.getView<View?>(R.id.image_view) as ImageView)
        }
      }
    } else if (getItemViewType(position) == VIEW_TYPE_LOADING) {
      holder.setVisibility(R.id.load_more_textview, View.VISIBLE)
      holder.setVisibility(R.id.progressBar, View.GONE)

      // TODO should define setOnClickListener in ViewHolder
      holder.getView<View?>(R.id.load_more_textview)?.setOnClickListener {
        holder.setVisibility(R.id.load_more_textview, View.GONE)
        holder.setVisibility(R.id.progressBar, View.VISIBLE)
//        callback?.loadMore()
      }
    }
  }

  override fun getItemCount(): Int {
    // +1 for bottom loading item
    // TODO should check can load more image from API result
    return if (ArrayUtils.getLengthSafe(mImageList) % Constants.IMAGE_PER_PAGE == 0)
      ArrayUtils.getLengthSafe(mImageList) + 1
    else
      ArrayUtils.getLengthSafe(mImageList)
  }

  override fun getItemViewType(position: Int): Int {
    return if (ArrayUtils.getLengthSafe(mImageList) > position) VIEW_TYPE_ITEM else VIEW_TYPE_LOADING
  }

  fun updateList(imageList: MutableList<PixabayImageObject>) {
    Log.d(ImageListAdapter::class.java.simpleName, "updateList with ${imageList.size} images")
    mImageList.clear()
    mImageList.addAll(imageList)
    Log.d(ImageListAdapter::class.java.simpleName, "${mImageList.size} images after update")
  }
}
