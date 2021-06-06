package com.jajinba.pixabaydemo.view.fragment

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jajinba.pixabaydemo.R
import com.jajinba.pixabaydemo.adapter.ImageListAdapter

class ImageGridFragment : ListFragment() {
    private val mAdapter = ImageListAdapter(this@ImageGridFragment)
    override fun getAdapter(): ImageListAdapter {
        return mAdapter
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager? {
        return StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun getContentLayout(): Int {
        return R.layout.fragment_list
    }

    override fun onFailed(errorMsg: Int) {
        // TODO
    }

    companion object {
        private const val SPAN_COUNT = 2
        fun newInstance(): ImageGridFragment {
            return ImageGridFragment()
        }
    }
}