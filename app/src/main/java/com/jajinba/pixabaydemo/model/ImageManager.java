package com.jajinba.pixabaydemo.model;


import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class ImageManager extends Observable {
  private static final ImageManager ourInstance = new ImageManager();

  private List<PixabayImageObject> mImageList;
  private Map<String, List<PixabayImageObject>> mKeywordToImageListMap;

  public static ImageManager getInstance() {
    return ourInstance;
  }

  private ImageManager() {
    mImageList = new ArrayList<>();
    mKeywordToImageListMap = new HashMap<>();
  }

  private void setImageList(List<PixabayImageObject> imageList) {
    mImageList = imageList;

    setChanged();
    notifyObservers();
  }

  public void setImageList(String key, List<PixabayImageObject> imageList) {
    setImageList(imageList);

    mKeywordToImageListMap.put(key, imageList);
  }

  public List<PixabayImageObject> getImageList() {
    return mImageList == null ? new ArrayList<PixabayImageObject>() : mImageList;
  }

  public boolean hasKeywordSearchBefore(String keyword) {
    return mKeywordToImageListMap.containsKey(keyword);
  }

  public void setImageListWithKeyword(String keyword) {
    if (TextUtils.isEmpty(keyword)) {
      return;
    }

    setImageList(mKeywordToImageListMap.get(keyword));
  }

}
