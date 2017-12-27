package com.jajinba.pixabaydemo.view.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.adapter.ImageListAdapter;

public class ImageListFragment extends ListFragment {

  public static ImageListFragment newInstance() {
    return new ImageListFragment();
  }

  @Override
  protected ImageListAdapter getAdapter() {
    return new ImageListAdapter(ImageListFragment.this, mImageList);
  }

  @Override
  protected RecyclerView.LayoutManager getLayoutManager() {
    return new LinearLayoutManager(getContext());
  }

  @Override
  protected int getContentLayout() {
    return R.layout.fragment_list;
  }

}
