package com.jajinba.pixabaydemo.presenter;


import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;

import com.jajinba.pixabaydemo.MainApplication;
import com.jajinba.pixabaydemo.contract.MainActivityContract;
import com.jajinba.pixabaydemo.model.ImageManager;

public class MainActivityPresenter implements MainActivityContract.Presenter {

  private static final String TAG = MainActivityPresenter.class.getSimpleName();

  private MainActivityContract.View mView;

  public MainActivityPresenter(MainActivityContract.View view) {
    mView = view;
    ImageManager.getInstance().addSearchCallback(new ImageManager.SearchCallback() {
      @Override
      public void onSuccess() {
        mView.searchDone();
      }

      @Override
      public void onFail(@StringRes int errorMsg) {
        mView.searchDone();
        mView.showErrorDialog(MainApplication.getInstance().getString(errorMsg));
      }
    });
  }

  @Override
  public void search(final @NonNull String keyword) {
    if (ImageManager.getInstance().hasKeywordSearchBefore(keyword)) {
      Log.d(TAG, "Keyword been search before, use previous list instead");
      ImageManager.getInstance().setCurrentKeyword(keyword);
    } else {
      mView.searchStart();
      ImageManager.getInstance().searchImage(keyword);
    }
  }

}
