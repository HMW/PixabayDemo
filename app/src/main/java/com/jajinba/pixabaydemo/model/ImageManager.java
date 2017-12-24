package com.jajinba.pixabaydemo.model;


import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ImageManager extends Observable {
  private static final ImageManager ourInstance = new ImageManager();

  private List<PixabayImageObject> mImageList;

  public static ImageManager getInstance() {
    return ourInstance;
  }

  private ImageManager() {
    mImageList = new ArrayList<>();
  }

  public void setImageList(List<PixabayImageObject> imageList) {
    mImageList = imageList;

    setChanged();
    notifyObservers();
  }

  @Nullable
  public List<PixabayImageObject> getImageList() {
    return mImageList;
  }

}
