package com.jajinba.pixabaydemo.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jajinba.pixabaydemo.utils.ArrayUtils;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

  private List<Fragment> mFragmentList;

  public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
    super(fm);

    mFragmentList = fragmentList;
  }

  @Override
  public Fragment getItem(int position) {
    return ArrayUtils.isEmpty(mFragmentList) ? null : mFragmentList.get(position);
  }

  @Override
  public int getCount() {
    return ArrayUtils.getLengthSafe(mFragmentList);
  }

  @Override
  public CharSequence getPageTitle(int position) {
    return "page " + ArrayUtils.getLengthSafe(mFragmentList);
  }
}
