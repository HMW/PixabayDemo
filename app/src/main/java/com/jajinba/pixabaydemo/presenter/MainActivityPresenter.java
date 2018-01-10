package com.jajinba.pixabaydemo.presenter;


import android.support.annotation.NonNull;
import android.util.Log;

import com.jajinba.pixabaydemo.MainApplication;
import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.contract.MainActivityContract;
import com.jajinba.pixabaydemo.model.ImageManager;
import com.jajinba.pixabaydemo.model.PixabayResponseObject;
import com.jajinba.pixabaydemo.network.ApiClient;
import com.jajinba.pixabaydemo.utils.ArrayUtils;
import com.jajinba.pixabaydemo.utils.SearchUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class MainActivityPresenter implements MainActivityContract.Presenter {

  private static final String TAG = MainActivityPresenter.class.getSimpleName();

  private MainActivityContract.View mView;

  private String mSearchKeyword = "";

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

      // TODO handle all api query together, maybe a Handler and a HandlerThread
      new Thread(new Runnable() {
        @Override
        public void run() {
          mSearchKeyword = keyword;

          Log.d(TAG, "Search image with keyword: " + SearchUtils.formatSearchKeyword(keyword));
          ApiClient.getInstance().searchImages(keyword, mObserver);
        }
      }).start();
    }
  }

  private Observer<Response<PixabayResponseObject>> mObserver =
      new Observer<Response<PixabayResponseObject>>() {
    @Override
    public void onSubscribe(Disposable d) {
      Log.d(TAG, "onSubscribe");
    }

    @Override
    public void onNext(Response<PixabayResponseObject> response) {
      if (response == null) {
        Log.e(TAG, "Response null");
        return;
      }

      if (response.isSuccessful()) {
        PixabayResponseObject object = response.body();
        if (object == null) {
          Log.e(TAG, "Response body null");
          return;
        }

        mView.searchDone();

        if (ArrayUtils.isNotEmpty(object.getHits())) {
          Log.d(TAG, "Received " + ArrayUtils.getLengthSafe(object.getHits()) + " images");
        } else {
          Log.d(TAG, "Received empty image list");
          mView.showErrorDialog(MainApplication.getInstance().getString(
              R.string.no_image_found));
        }

        ImageManager.getInstance().setImageList(mSearchKeyword, object.getHits());
        mSearchKeyword = "";
      } else {
        Log.e(TAG, "error msg: " + response.errorBody());
      }
    }

    @Override
    public void onError(Throwable e) {
      Log.e(TAG, "error msg: " + e.getMessage());
    }

    @Override
    public void onComplete() {
      Log.d(TAG, "onComplete");
    }
  };

}
