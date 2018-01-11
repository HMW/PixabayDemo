package com.jajinba.pixabaydemo.presenter;


import android.support.annotation.NonNull;
import android.util.Log;

import com.jajinba.pixabaydemo.contract.MainActivityContract;
import com.jajinba.pixabaydemo.model.ImageManager;

public class MainActivityPresenter implements MainActivityContract.Presenter {

  private static final String TAG = MainActivityPresenter.class.getSimpleName();

  private MainActivityContract.View mView;

  public MainActivityPresenter(MainActivityContract.View view) {
    mView = view;
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
