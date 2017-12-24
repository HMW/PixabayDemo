package com.jajinba.pixabaydemo.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.model.PixabayImageObject;
import com.jajinba.pixabaydemo.presenter.ListPresenter;
import com.jajinba.pixabaydemo.utils.ArrayUtils;

import java.util.List;

import butterknife.BindView;

public abstract class ListFragment extends BaseFragment {

  private static final String TAG = ListFragment.class.getSimpleName();

  @BindView(R.id.recycler_view)
  RecyclerView mRecyclerView;

  private ListPresenter mPresenter;
  protected List<PixabayImageObject> mImageList;

  private ListPresenter.Callback mCallback = new ListPresenter.Callback() {
    @Override
    public void updateImageList(List<PixabayImageObject> imageList) {
      mImageList = imageList;

      // FIXME check is attached?
      if (isFragmentValid()) {
        Log.d(TAG, ArrayUtils.getLengthSafe(imageList) + "image received");

        mRecyclerView.setAdapter(getAdapter());
        mRecyclerView.setLayoutManager(getLayoutManager());
      }
    }
  };

  protected abstract RecyclerView.Adapter getAdapter();

  protected abstract RecyclerView.LayoutManager getLayoutManager();

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
