package com.jajinba.pixabaydemo.presenter;

import com.jajinba.pixabaydemo.model.ImageManager;
import com.jajinba.pixabaydemo.model.PixabayImageObject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ListPresenter implements Observer {

  public interface Callback {
    void updateImageList(String keyword, List<PixabayImageObject> imageList);
  }

  // FIXME weakreference
  private Callback mCallback;

  public ListPresenter(Callback callback) {
    mCallback = callback;

    ImageManager.getInstance().addObserver(this);
  }

  @Override
  public void update(Observable observable, Object o) {
    if (mCallback != null) {
      String currentKeyword = ImageManager.getInstance().getCurrentKeyword();

      mCallback.updateImageList(currentKeyword,
          ImageManager.getInstance().getImageListWithKeyword(currentKeyword));
    }
  }
}
