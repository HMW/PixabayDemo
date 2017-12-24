package com.jajinba.pixabaydemo.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.adapter.ImageListAdapter;
import com.jajinba.pixabaydemo.model.PixabayImageObject;
import com.jajinba.pixabaydemo.presenter.ListPresenter;
import com.jajinba.pixabaydemo.utils.ArrayUtils;

import java.util.List;

import butterknife.BindView;

public class ImageListFragment extends ListFragment {

  private static final String TAG = ImageListFragment.class.getSimpleName();

  @BindView(R.id.recycler_view)
  RecyclerView mRecyclerView;

  private ListPresenter mPresenter;

  private ListPresenter.Callback mCallback = new ListPresenter.Callback() {
    @Override
    public void updateImageList(List<PixabayImageObject> imageList) {
      // FIXME check is attached?
      if (isFragmentValid()) {
        Log.d(TAG, "ImageListFragment " + ArrayUtils.getLengthSafe(imageList) + "image received");

        // FIXME extract ImageListAdapter & LinearLayoutManager as member variable
        mRecyclerView.setAdapter(new ImageListAdapter(ImageListFragment.this, imageList));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      }
    }
  };

  public static ImageListFragment newInstance() {
    return new ImageListFragment();
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
  }
}
