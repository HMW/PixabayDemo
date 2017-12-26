package com.jajinba.pixabaydemo.view.fragment;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.adapter.ImageListAdapter;

import butterknife.BindView;

public class ImageGridFragment extends ListFragment {

  @BindView(R.id.recycler_view)
  RecyclerView mRecyclerView;

  private ImageListAdapter mAdapter;
  private RecyclerView.LayoutManager mLayoutManager;

  public static ImageGridFragment newInstance() {
    return new ImageGridFragment();
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
      mAdapter = new ImageListAdapter(ImageGridFragment.this, mImageList);
    }

    return mAdapter;
  }

  private RecyclerView.LayoutManager getMemberLayoutManager() {
    if (mLayoutManager == null) {
      mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }

    return mLayoutManager;
  }
}
