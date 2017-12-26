package com.jajinba.pixabaydemo.model;


import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class ImageManager extends Observable {
  private static final ImageManager ourInstance = new ImageManager();

  private String mCurrentKeyword;
  private Map<String, List<PixabayImageObject>> mKeywordToImageListMap;

  public static ImageManager getInstance() {
    return ourInstance;
  }

  private ImageManager() {
    mKeywordToImageListMap = new HashMap<>();
  }

  public void setCurrentKeyword(String keyword) {
    mCurrentKeyword = keyword;

    setChanged();
    notifyObservers();
  }

  public String getCurrentKeyword() {
    return mCurrentKeyword;
  }

  public void setImageList(String keyword, List<PixabayImageObject> imageList) {
    mKeywordToImageListMap.put(keyword, imageList);
    setCurrentKeyword(keyword);
  }

  public boolean hasKeywordSearchBefore(String keyword) {
    return mKeywordToImageListMap.containsKey(keyword);
  }

  public List<PixabayImageObject> getImageListWithKeyword(String keyword) {
    if (TextUtils.isEmpty(keyword) || mKeywordToImageListMap.containsKey(keyword) == false) {
      return new ArrayList<>();
    }

    return mKeywordToImageListMap.get(keyword);
  }

}
