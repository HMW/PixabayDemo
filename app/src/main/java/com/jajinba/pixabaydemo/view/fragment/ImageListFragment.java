package com.jajinba.pixabaydemo.view.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.adapter.ImageListAdapter;

public class ImageListFragment extends ListFragment {

  private ImageListAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;

  public static ImageListFragment newInstance() {
    return new ImageListFragment();
  }

  @Override
  protected ImageListAdapter getAdapter() {
    return getMemberAdapter();
  }

  @Override
  protected RecyclerView.LayoutManager getLayoutManager() {
    return getMemberLayoutManager();
  }

  @Override
  protected int getContentLayout() {
    return R.layout.fragment_list;
  }

  private ImageListAdapter getMemberAdapter() {
    if (mAdapter == null) {
      mAdapter = new ImageListAdapter(ImageListFragment.this, mImageList);
    }

    return mAdapter;
  }

  private RecyclerView.LayoutManager getMemberLayoutManager() {
    if (mLayoutManager == null) {
      mLayoutManager = new LinearLayoutManager(getContext());
    }

    return mLayoutManager;
  }
}
