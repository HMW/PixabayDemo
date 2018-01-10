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

  private Observer<PixabayResponseObject> mObserver = new Observer<PixabayResponseObject>() {
    @Override
    public void onSubscribe(Disposable d) {
      Log.d(TAG, "onSubscribe");
    }

    @Override
    public void onNext(PixabayResponseObject object) {
      if (object == null) {
        Log.e(TAG, "Response null");
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
    }

    @Override
    public void onError(Throwable e) {
      /*
          Log.e(TAG, "error msg: " + errorMsg);

          mView.searchDone();

          // TODO should use ConnectivityManager to check whether has internet connection
          if (errorMsg.contains(Constants.FAIL_TO_CONNECT_TO_SERVER)) {
            mView.showErrorDialog(MainApplication.getInstance().getString(
                R.string.connect_to_server_fail));
          }
        }
      */
    }

    @Override
    public void onComplete() {
      Log.d(TAG, "onComplete");
    }
  };

}
