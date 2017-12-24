package com.jajinba.pixabaydemo.view.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.adapter.ImageListAdapter;

import butterknife.BindView;

public class ImageListFragment extends ListFragment {

  @BindView(R.id.recycler_view)
  RecyclerView mRecyclerView;

  public static ImageListFragment newInstance() {
    return new ImageListFragment();
  }

  @Override
  protected RecyclerView.Adapter getAdapter() {
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
