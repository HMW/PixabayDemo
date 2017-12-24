package com.jajinba.pixabaydemo.presenter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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
  }

  private Callback mCallback;

  public MainActivityPresenter(Callback callback) {
    mCallback = callback;
  }

  public void onSearchClick(final @NonNull String keyword) {
    if (mCallback != null) {
      mCallback.searchStart();
    }

    new Thread(new Runnable() {
      @Override
      public void run() {
        HashMap<String, String> filterMap = new HashMap<>();
        filterMap.put(API_KEY, "7486024-feb89a76e79a6ce60b46eeee7");// FIXME replace with key
        filterMap.put(API_KEYWORD, formatSearchKeyword(keyword));

        Log.d(TAG, "Search image with keyword: " + formatSearchKeyword(keyword));
        ApiClient.getInstance().getImages(filterMap, responseListener);
      }
    }).start();
  }

  private ResponseListener responseListener = new ResponseListener() {
    @Override
    public void onSuccess(@Nullable Object object) {
      if (object == null) {
        Log.e(TAG, "Response null");
      }

      try {
        if (ArrayUtils.isNotEmpty(((PixabayResponseObject) object).getHits())) {
          Log.d(TAG, "Received " + ArrayUtils.getLengthSafe(
              ((PixabayResponseObject) object).getHits()) + " images");

          ImageManager.getInstance().setImageList(((PixabayResponseObject) object).getHits());
        } else {
          Log.e(TAG, "Received empty image list");
        }
      } catch (ClassCastException e) {
        Log.e(TAG, "ClassCastException msg " + e.getMessage());
      }

      if (mCallback != null) {
        mCallback.searchDone();
      }
    }

    @Override
    public void onFailure(String errorMsg) {
      Log.e(TAG, "error msg: " + errorMsg);

      if (mCallback != null) {
        mCallback.searchDone();
      }
    }
  };

  private String formatSearchKeyword(String keyword) {
    return keyword.contains(" ") ? keyword.replace(" ", "+") : keyword;
  }
}
