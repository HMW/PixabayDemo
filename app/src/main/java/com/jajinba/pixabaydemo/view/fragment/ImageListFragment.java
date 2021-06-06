package com.jajinba.pixabaydemo.view.fragment;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.adapter.ImageListAdapter;

public class ImageListFragment extends ListFragment {

  private ImageListAdapter mAdapter;

  public static ImageListFragment newInstance() {
    return new ImageListFragment();
  }

  @Override
  protected ImageListAdapter getAdapter() {
    if (mAdapter == null) {
      mAdapter = new ImageListAdapter(ImageListFragment.this);
    }

    return mAdapter;
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
