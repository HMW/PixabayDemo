package com.jajinba.pixabaydemo.presenter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.jajinba.pixabaydemo.model.ImageManager;
import com.jajinba.pixabaydemo.model.PixabayImageObject;
import com.jajinba.pixabaydemo.model.PixabayResponseObject;
import com.jajinba.pixabaydemo.network.ApiClient;
import com.jajinba.pixabaydemo.network.listener.ResponseListener;
import com.jajinba.pixabaydemo.utils.ArrayUtils;
import com.jajinba.pixabaydemo.utils.SearchUtils;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ListPresenter implements Observer {

  private static final String TAG = ListPresenter.class.getSimpleName();

  public interface Callback {
    void updateImageList(String keyword, List<PixabayImageObject> imageList);
  }

  // FIXME weakreference
  private Callback mCallback;
  private String mSearchKeyword;

  public ListPresenter(Callback callback) {
    mCallback = callback;

    ImageManager.getInstance().addObserver(this);
  }

  public void loadMore(final String keyword) {
    // TODO handle all api query together, maybe a Handler and a HandlerThread
    new Thread(new Runnable() {
      @Override
      public void run() {
        mSearchKeyword = keyword;
        int searchPage = ImageManager.getInstance().previousLoadedPage(mSearchKeyword) + 1;
        Log.d(TAG, "Search image with keyword: " + SearchUtils.formatSearchKeyword(mSearchKeyword) +
        ", page: " + searchPage);

        ApiClient.getInstance().searchImages(mSearchKeyword, searchPage, responseListener);
      }
    }).start();
  }

  @Override
  public void update(Observable observable, Object o) {
    if (mCallback != null) {
      String currentKeyword = ImageManager.getInstance().getCurrentKeyword();

      mCallback.updateImageList(currentKeyword,
          ImageManager.getInstance().getImageListWithKeyword(currentKeyword));
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

      if (ArrayUtils.isNotEmpty(object.getHits())) {
        Log.d(TAG, "Received " + ArrayUtils.getLengthSafe(object.getHits()) + " images");

        ImageManager.getInstance().setImageList(mSearchKeyword, object.getHits());

        mSearchKeyword = "";
      } else {
        Log.d(TAG, "Received empty image list");

        // TODO show dialog with error message
      }
    }

        @Override
        public void onFailure(String errorMsg) {
          Log.e(TAG, "error msg: " + errorMsg);

          // TODO show dialog with error message
        }
  };
}
