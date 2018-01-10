package com.jajinba.pixabaydemo.presenter;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.jajinba.pixabaydemo.MainApplication;
import com.jajinba.pixabaydemo.contract.ListContract;
import com.jajinba.pixabaydemo.model.ImageManager;
import com.jajinba.pixabaydemo.model.PixabayImageObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ListPresenter implements Observer, ListContract.Presenter {

  private ListContract.View mView;

  public ListPresenter(ListContract.View view) {
    mView = view;
    ImageManager.getInstance().addObserver(this);
    ImageManager.getInstance().setSearchCallback(new ImageManager.SearchCallback() {
      @Override
      public void onFail(@StringRes int errorMsg) {
        mView.searchFinished();
        mView.showErrorMsgDialog(MainApplication.getInstance().getString(errorMsg));
      }
    });
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
    int searchPage = ImageManager.getInstance().previousLoadedPage(keyword) + 1;
    ImageManager.getInstance().searchImage(keyword, searchPage);
  }

  @Override
  public String getLastOperation() {
    return ImageManager.getInstance().getLastOperation();
  }

}
