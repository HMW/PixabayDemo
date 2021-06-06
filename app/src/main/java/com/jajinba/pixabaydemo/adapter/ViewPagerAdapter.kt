package com.jajinba.pixabaydemo.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.jajinba.pixabaydemo.R
import com.jajinba.pixabaydemo.utils.ArrayUtils

class ViewPagerAdapter(
    fm: FragmentManager,
    private val mContext: Context,
    private val mFragmentList: MutableList<Fragment>
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return ArrayUtils.getLengthSafe(mFragmentList)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.getString(if (position == 0) R.string.tab_title_list else R.string.tab_title_grid)
    }
}