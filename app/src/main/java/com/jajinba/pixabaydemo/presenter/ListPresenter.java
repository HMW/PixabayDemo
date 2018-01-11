package com.jajinba.pixabaydemo.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.jajinba.pixabaydemo.contract.ListContract;
import com.jajinba.pixabaydemo.model.ImageManager;
import com.jajinba.pixabaydemo.model.PixabayImageObject;
import com.jajinba.pixabaydemo.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class ListPresenter implements  ListContract.Presenter {
  private static final String TAG = ListPresenter.class.getSimpleName();

  private ListContract.View mView;

  public ListPresenter(ListContract.View view) {
    mView = view;

    ImageManager.getInstance().setCallback(new ImageManager.Callback() {
      @Override
      public void onSuccess(String keyword, List<PixabayImageObject> imageList) {
        Log.d(TAG, "onSuccess, image count:" + ArrayUtils.getLengthSafe(imageList));
        mView.searchFinished(keyword, imageList);
      }

      @Override
      public void onFailed(int errorMsg) {
        Log.d(TAG, "onFailed");
        mView.searchFinished(null, null);
      }
    });
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
    int searchPage = ImageManager.getInstance().previousLoadedPage(keyword) + 1;
    ImageManager.getInstance().searchImage(keyword, searchPage);
  }

  @Override
  public String getLastOperation() {
    return ImageManager.getInstance().getLastOperation();
  }

}
