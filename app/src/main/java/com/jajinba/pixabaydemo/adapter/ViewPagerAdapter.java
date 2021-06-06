package com.jajinba.pixabaydemo.adapter;


import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.utils.ArrayUtils;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

  private Context mContext;
  private List<Fragment> mFragmentList;

  public ViewPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList) {
    super(fm);

    mContext = context;
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
    return mContext.getString(position == 0 ? R.string.tab_title_list : R.string.tab_title_grid);
  }
}
