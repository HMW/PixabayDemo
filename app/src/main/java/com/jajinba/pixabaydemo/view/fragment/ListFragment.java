package com.jajinba.pixabaydemo.view.fragment;

import com.jajinba.pixabaydemo.model.PixabayImageObject;
import com.jajinba.pixabaydemo.presenter.ListPresenter;

import java.util.List;

public abstract class ListFragment extends BaseFragment {

  private ListPresenter mPresenter;

  public ListFragment() {
    mPresenter = new ListPresenter(mCallback);
  }

  private ListPresenter.Callback mCallback = new ListPresenter.Callback() {
    @Override
    public void imageReceived(List<PixabayImageObject> imageList) {

    }
  };

  // TODO extract list and grid fragment common method/step to here

}
