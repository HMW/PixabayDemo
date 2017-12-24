package com.jajinba.pixabaydemo.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.adapter.ImageListAdapter;
import com.jajinba.pixabaydemo.model.PixabayImageObject;
import com.jajinba.pixabaydemo.presenter.ListPresenter;

import java.util.List;

import butterknife.BindView;

public class ImageGridFragment extends BaseFragment {

  private static final String TAG = ImageGridFragment.class.getSimpleName();

  @BindView(R.id.recycler_view)
  RecyclerView mRecyclerView;

  private ListPresenter mPresenter;

  private ListPresenter.Callback mCallback = new ListPresenter.Callback() {
    @Override
    public void imageReceived(List<PixabayImageObject> imageList) {
      // FIXME check is attached?
      if (isFragmentValid()) {
        Log.d(TAG, "ImageListFragment imageReceived");

        // FIXME extract ImageListAdapter & LinearLayoutManager as member variable
        mRecyclerView.setAdapter(new ImageListAdapter(ImageGridFragment.this, imageList));
        mRecyclerView.setLayoutManager(
            new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
      }
    }
  };

  public static ImageGridFragment newInstance() {
    return new ImageGridFragment();
  }

  @Override
  protected int getContentLayout() {
    return R.layout.fragment_list;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (mPresenter == null) {
      mPresenter = new ListPresenter(mCallback);
    }

    mPresenter.onViewCreated();

    // TODO check has cached image list in ImageManager
    // TODO check the cached image list's key word
  }
}
