package com.jajinba.pixabaydemo.view.fragment;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.adapter.ImageListAdapter;


import butterknife.BindView;

public class ImageGridFragment extends ListFragment {

  @BindView(R.id.recycler_view)
  RecyclerView mRecyclerView;

  public static ImageGridFragment newInstance() {
    return new ImageGridFragment();
  }

  @Override
  protected RecyclerView.Adapter getAdapter() {
    return new ImageListAdapter(ImageGridFragment.this, mImageList);
  }

  @Override
  protected RecyclerView.LayoutManager getLayoutManager() {
    return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
  }

  @Override
  protected int getContentLayout() {
    return R.layout.fragment_list;
  }
}
