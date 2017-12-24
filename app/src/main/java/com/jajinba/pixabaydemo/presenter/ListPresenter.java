package com.jajinba.pixabaydemo.presenter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.jajinba.pixabaydemo.model.ImageManager;
import com.jajinba.pixabaydemo.model.PixabayImageObject;
import com.jajinba.pixabaydemo.model.PixabayResponseObject;
import com.jajinba.pixabaydemo.network.ApiClient;
import com.jajinba.pixabaydemo.network.listener.ResponseListener;
import com.jajinba.pixabaydemo.utils.ArrayUtils;

import java.util.HashMap;
import java.util.List;

public class ListPresenter {

  private static final String TAG = ListPresenter.class.getSimpleName();

  public interface Callback {
    void imageReceived(List<PixabayImageObject> imageList);
  }

  // FIXME weakreference
  private Callback mCallback;

  public ListPresenter(Callback callback) {
    mCallback = callback;
  }

  public void onViewCreated() {
    // FIXME remove after test
    new Thread(new Runnable() {
      @Override
      public void run() {
        HashMap<String, String> filterMap = new HashMap<>();
        filterMap.put("key", "7486024-feb89a76e79a6ce60b46eeee7");// FIXME replace with key
        filterMap.put("q", "yellow+flower");

        Log.e(TAG, "Presenter trigger api ");
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

          if (mCallback != null) {
            mCallback.imageReceived(((PixabayResponseObject) object).getHits());
          }
        } else {
          Log.e(TAG, "Received empty image list");
        }
      } catch (ClassCastException e) {
        Log.e(TAG, "ClassCastException msg " + e.getMessage());
      }
    }

    @Override
    public void onFailure(String errorMsg) {
      Log.e(TAG, "error msg: " + errorMsg);
    }
  };
}
