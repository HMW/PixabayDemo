package com.jajinba.pixabaydemo.adapter

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
import com.jajinba.pixabaydemo.view.fragment.ListFragment

class ImageListAdapter(private val mFragment: ListFragment) : RecyclerView.Adapter<ViewHolder?>() {

    companion object {
        private const val VIEW_TYPE_ITEM = 5566
        private const val VIEW_TYPE_LOADING = 5567
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
            if (mFragment.isFragmentValid() && ArrayUtils.getLengthSafe(mImageList) > position) {
                Glide.with(mFragment)
                    .load(mImageList[position].webformatUrl)
                    .placeholder(R.drawable.placeholder) // workaround, refer glide issue 542 on Github
                    .dontAnimate()
                    .override(
                        mImageList[position].webformatWidth,
                        mImageList[position].webformatHeight
                    )
                    .into(holder.getView<View?>(R.id.image_view) as ImageView)
            }
        } else if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            holder.setVisibility(R.id.load_more_textview, View.VISIBLE)
            holder.setVisibility(R.id.progressBar, View.GONE)

            // TODO should define setOnClickListener in ViewHolder
            holder.getView<View?>(R.id.load_more_textview)?.setOnClickListener {
                holder.setVisibility(R.id.load_more_textview, View.GONE)
                holder.setVisibility(R.id.progressBar, View.VISIBLE)
                mFragment.loadMore()
            }
        }
    }

    override fun getItemCount(): Int {
        // +1 for bottom loading item
        return if (ArrayUtils.getLengthSafe(mImageList) % Constants.IMAGE_PER_PAGE == 0) ArrayUtils.getLengthSafe(
            mImageList
        ) + 1 else ArrayUtils.getLengthSafe(mImageList)
    }

    override fun getItemViewType(position: Int): Int {
        return if (ArrayUtils.getLengthSafe(mImageList) > position) VIEW_TYPE_ITEM else VIEW_TYPE_LOADING
    }

    fun updateList(imageList: MutableList<PixabayImageObject>) {
        mImageList.clear()
        Log.d(ImageListAdapter::class.java.simpleName, "updateList with ${imageList.size} images")
        mImageList.addAll(imageList)
    }
}
