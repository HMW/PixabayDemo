package com.jajinba.pixabaydemo.view.fragment;


import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.adapter.ImageListAdapter;

public class ImageGridFragment extends ListFragment {

  private static final int SPAN_COUNT = 2;

  private ImageListAdapter mAdapter;

  public static ImageGridFragment newInstance() {
    return new ImageGridFragment();
  }

  @Override
  protected ImageListAdapter getAdapter() {
    if (mAdapter == null) {
      mAdapter = new ImageListAdapter(ImageGridFragment.this);
    }

    return mAdapter;
  }

  @Override
  protected RecyclerView.LayoutManager getLayoutManager() {
    return new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
  }

  @Override
  protected int getContentLayout() {
    return R.layout.fragment_list;
  }

}
