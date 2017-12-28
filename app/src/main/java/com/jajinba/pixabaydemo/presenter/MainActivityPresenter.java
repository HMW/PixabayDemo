package com.jajinba.pixabaydemo.presenter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jajinba.pixabaydemo.Constants;
import com.jajinba.pixabaydemo.MainApplication;
import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.model.ImageManager;
import com.jajinba.pixabaydemo.model.PixabayResponseObject;
import com.jajinba.pixabaydemo.network.ApiClient;
import com.jajinba.pixabaydemo.network.listener.ResponseListener;
import com.jajinba.pixabaydemo.utils.ArrayUtils;
import com.jajinba.pixabaydemo.utils.SearchUtils;

public class MainActivityPresenter {

  private static final String TAG = MainActivityPresenter.class.getSimpleName();

  public interface Callback {
    void searchStart();

    void searchDone();

    void showErrorDialog(String errorMsg);
  }

  private Callback mCallback;
  private String mSearchKeyword = "";

  public MainActivityPresenter(Callback callback) {
    mCallback = callback;
  }

  public void onSearchClick(final @NonNull String keyword) {
    if (ImageManager.getInstance().hasKeywordSearchBefore(keyword)) {
      Log.d(TAG, "Keyword been search before, use previous list instead");
      ImageManager.getInstance().setCurrentKeyword(keyword);
    } else {
      if (mCallback != null) {
        mCallback.searchStart();
      }

      // TODO handle all api query together, maybe a Handler and a HandlerThread
      new Thread(new Runnable() {
        @Override
        public void run() {
          mSearchKeyword = keyword;

          Log.d(TAG, "Search image with keyword: " + SearchUtils.formatSearchKeyword(keyword));
          ApiClient.getInstance().searchImages(keyword, responseListener);
        }
      }).start();
    }
  }

  private ResponseListener<PixabayResponseObject> responseListener =
      new ResponseListener<PixabayResponseObject>() {

        @Override
        public void onSuccess(@Nullable PixabayResponseObject object) {
          if (object == null) {
            Log.e(TAG, "Response null");
            return;
          }

          if (mCallback != null) {
            mCallback.searchDone();
          }

          if (ArrayUtils.isNotEmpty(object.getHits())) {
            Log.d(TAG, "Received " + ArrayUtils.getLengthSafe(object.getHits()) + " images");
          } else {
            Log.d(TAG, "Received empty image list");
            if (mCallback != null) {
              mCallback.showErrorDialog(MainApplication.getInstance().getString(
                  R.string.no_image_found));
            }
          }

          ImageManager.getInstance().setImageList(mSearchKeyword, object.getHits());
          mSearchKeyword = "";
        }

        @Override
        public void onFailure(String errorMsg) {
          Log.e(TAG, "error msg: " + errorMsg);

          if (mCallback != null) {
            mCallback.searchDone();

            // TODO should use ConnectivityManager to check whether has internet connection
            if (errorMsg.contains(Constants.FAIL_TO_CONNECT_TO_SERVER)) {
              mCallback.showErrorDialog(MainApplication.getInstance().getString(
                  R.string.connect_to_server_fail));
            }
          }
        }
      };
}
