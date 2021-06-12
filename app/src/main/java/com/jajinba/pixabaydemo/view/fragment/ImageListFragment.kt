package com.jajinba.pixabaydemo.view.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jajinba.pixabaydemo.adapter.ImageListAdapter

class ImageListFragment : ListFragment() {
    private val mAdapter = ImageListAdapter(this@ImageListFragment)

    override fun getAdapter(): ImageListAdapter {
        return mAdapter
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }

    override fun onFailed(errorMsg: Int) {
        // TODO
    }

    companion object {
        fun newInstance(): ImageListFragment {
            return ImageListFragment()
        }
    }
}
