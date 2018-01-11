package com.jajinba.pixabaydemo.presenter;


import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.jajinba.pixabaydemo.contract.MainActivityContract;
import com.jajinba.pixabaydemo.model.ImageManager;
import com.jajinba.pixabaydemo.model.PixabayImageObject;

import java.util.List;

public class MainActivityPresenter implements MainActivityContract.Presenter {

  private MainActivityContract.View mView;

  public MainActivityPresenter(MainActivityContract.View view) {
    mView = view;

    ImageManager.getInstance().setCallback(new ImageManager.Callback() {
      @Override
      public void onSuccess(String keyword, List<PixabayImageObject> imageList) {
        mView.searchFinished(true, -1);
      }

      @Override
      public void onFailed(@StringRes int errorMsg) {
        mView.searchFinished(false, errorMsg);
      }
    });
  }

  @Override
  public void search(final @NonNull String keyword) {
    mView.searchStart();
    ImageManager.getInstance().searchImage(keyword);
  }

}
