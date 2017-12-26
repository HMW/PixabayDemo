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

import java.util.HashMap;

public class MainActivityPresenter {

  private static final String TAG = MainActivityPresenter.class.getSimpleName();
  private static final String API_KEY = "key";
  private static final String API_KEYWORD = "q";

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

          HashMap<String, String> filterMap = new HashMap<>();
          filterMap.put(API_KEY, MainApplication.getInstance().getString(R.string.pixabay_api_key));
          filterMap.put(API_KEYWORD, formatSearchKeyword(keyword));

          Log.d(TAG, "Search image with keyword: " + formatSearchKeyword(keyword));
          ApiClient.getInstance().getImages(filterMap, responseListener);
        }
      }).start();
    }
  }

  private ResponseListener responseListener = new ResponseListener() {
    @Override
    public void onSuccess(@Nullable Object object) {
      if (object == null) {
        Log.e(TAG, "Response null");
      }

      if (mCallback != null) {
        mCallback.searchDone();
      }

      try {
        if (ArrayUtils.isNotEmpty(((PixabayResponseObject) object).getHits())) {
          Log.d(TAG, "Received " + ArrayUtils.getLengthSafe(
              ((PixabayResponseObject) object).getHits()) + " images");

          ImageManager.getInstance().setImageList(
              mSearchKeyword,
              ((PixabayResponseObject) object).getHits());

          mSearchKeyword = "";
        } else {
          Log.d(TAG, "Received empty image list");

          if (mCallback != null) {
            mCallback.showErrorDialog(MainApplication.getInstance().getString(
                R.string.no_image_found));
          }
        }
      } catch (ClassCastException e) {
        Log.e(TAG, "ClassCastException msg " + e.getMessage());
      }
    }

    @Override
    public void onFailure(String errorMsg) {
      Log.e(TAG, "error msg: " + errorMsg);

      if (mCallback != null) {
        mCallback.searchDone();

        if (errorMsg.contains(Constants.FAIL_TO_CONNECT_TO_SERVER)) {
          mCallback.showErrorDialog(MainApplication.getInstance().getString(
              R.string.connect_to_server_fail));
        }
      }
    }
  };

  private String formatSearchKeyword(String keyword) {
    return keyword.contains(" ") ? keyword.replace(" ", "+") : keyword;
  }
}
