package com.jajinba.pixabaydemo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jajinba.pixabaydemo.utils.ArrayUtils

class ViewPagerAdapter(
  fm: FragmentManager,
  private val mFragmentList: MutableList<Fragment>,
  lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

  override fun getItemCount(): Int {
    return ArrayUtils.getLengthSafe(mFragmentList)
  }

  override fun createFragment(position: Int): Fragment {
    return mFragmentList[position]
  }


}
