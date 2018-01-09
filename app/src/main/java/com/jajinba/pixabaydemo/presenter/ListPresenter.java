package com.jajinba.pixabaydemo.presenter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.jajinba.pixabaydemo.Constants;
import com.jajinba.pixabaydemo.MainApplication;
import com.jajinba.pixabaydemo.R;
import android.text.TextUtils;

import com.jajinba.pixabaydemo.contract.ListContract;
import com.jajinba.pixabaydemo.model.ImageManager;
import com.jajinba.pixabaydemo.model.PixabayImageObject;
import com.jajinba.pixabaydemo.model.PixabayResponseObject;
import com.jajinba.pixabaydemo.network.ApiClient;
import com.jajinba.pixabaydemo.network.listener.ResponseListener;
import com.jajinba.pixabaydemo.utils.ArrayUtils;
import com.jajinba.pixabaydemo.utils.SearchUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ListPresenter implements Observer, ListContract.Presenter {

  private static final String TAG = ListPresenter.class.getSimpleName();

  private ListContract.View mView;

  private String mSearchKeyword;

  public ListPresenter(ListContract.View view) {
    mView = view;
    ImageManager.getInstance().addObserver(this);
  }

  @Override
  public void update(Observable observable, Object o) {
    String currentKeyword = ImageManager.getInstance().getCurrentKeyword();

    mView.updateImageList(currentKeyword,
        ImageManager.getInstance().getImageListWithKeyword(currentKeyword));
  }

  @Override
  public List<PixabayImageObject> getImageList(String keyword) {
    if (TextUtils.isEmpty(keyword)) {
      return new ArrayList<>();
    } else {
      return ImageManager.getInstance().getImageListWithKeyword(keyword);
    }
  }


  @Override
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
  public String getLastOperation() {
    return ImageManager.getInstance().getLastOperation();
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

            mView.showErrorMsgDialog(MainApplication.getInstance().getString(
                R.string.no_image_found));
          }
        }

        @Override
        public void onFailure(String errorMsg) {
          Log.e(TAG, "error msg: " + errorMsg);

          if (errorMsg.contains(Constants.FAIL_TO_CONNECT_TO_SERVER)) {
            mView.showErrorMsgDialog(MainApplication.getInstance().getString(
                R.string.connect_to_server_fail));
          }
        }
      };
}
