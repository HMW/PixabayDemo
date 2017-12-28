package com.jajinba.pixabaydemo.model;


import android.support.annotation.StringDef;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class ImageManager extends Observable {
  private static final ImageManager ourInstance = new ImageManager();

  public static final String NEW_SEARCH = "new.search";
  public static final String LOAD_MORE = "load.more";

  @StringDef({
      NEW_SEARCH,
      LOAD_MORE
  })
  public @interface Operation {

  }

  private String mCurrentKeyword;
  private
  @ImageManager.Operation
  String mLastOperation;
  private Map<String, List<PixabayImageObject>> mKeywordToImageListMap;
  private Map<String, Integer> mKeywordToLoadedPageMap;

  public static ImageManager getInstance() {
    return ourInstance;
  }

  private ImageManager() {
    mKeywordToImageListMap = new HashMap<>();
    mKeywordToLoadedPageMap = new HashMap<>();
  }

  public void setCurrentKeyword(String keyword) {
    mCurrentKeyword = keyword;

    setChanged();
    notifyObservers();
  }

  public
  @Operation
  String getLastOperation() {
    return mLastOperation;
  }

  public String getCurrentKeyword() {
    return mCurrentKeyword;
  }

  public void setImageList(String keyword, List<PixabayImageObject> imageList) {
    // update image list
    if (mKeywordToImageListMap.containsKey(keyword)) {
      // put new list to previous list to keep image order
      mKeywordToImageListMap.get(keyword).addAll(imageList);
      mLastOperation = LOAD_MORE;
    } else {
      mKeywordToImageListMap.put(keyword, imageList);
      mLastOperation = NEW_SEARCH;
    }

    // update loaded page
    if (mKeywordToLoadedPageMap.containsKey(keyword)) {
      mKeywordToLoadedPageMap.put(keyword, mKeywordToLoadedPageMap.get(keyword) + 1);
    } else {
      mKeywordToLoadedPageMap.put(keyword, 1);
    }

    setCurrentKeyword(keyword);
  }

  public boolean hasKeywordSearchBefore(String keyword) {
    return mKeywordToImageListMap.containsKey(keyword);
  }

  public int previousLoadedPage(String keyword) {
    if (mKeywordToLoadedPageMap.containsKey(keyword) == false) {
      return 0;
    }

    return mKeywordToLoadedPageMap.get(keyword);
  }

  public List<PixabayImageObject> getImageListWithKeyword(String keyword) {
    if (TextUtils.isEmpty(keyword) || mKeywordToImageListMap.containsKey(keyword) == false) {
      return new ArrayList<>();
    }

    return mKeywordToImageListMap.get(keyword);
  }

}
