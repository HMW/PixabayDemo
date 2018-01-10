package com.jajinba.pixabaydemo.model;


import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import com.jajinba.pixabaydemo.Constants;
import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.network.ApiClient;
import com.jajinba.pixabaydemo.utils.ArrayUtils;
import com.jajinba.pixabaydemo.utils.SearchUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

public class ImageManager extends Observable {
  private static final String TAG = ImageManager.class.getSimpleName();

  private static final ImageManager ourInstance = new ImageManager();

  public static final String NEW_SEARCH = "new.search";
  public static final String LOAD_MORE = "load.more";

  @StringDef({
      NEW_SEARCH,
      LOAD_MORE
  })
  public @interface Operation {

  }

  public interface SearchCallback {
    void onSuccess();
    void onFail(@StringRes int  errorMsg);
  }

  private
  @ImageManager.Operation
  String mLastOperation;
  private String mCurrentKeyword;// FIXME better naming
  private String mSearchingKeyword;// FIXME better naming
  private Map<String, List<PixabayImageObject>> mKeywordToImageListMap;
  private Map<String, Integer> mKeywordToLoadedPageMap;
  private List<SearchCallback> mCallbackList;// FIXME should have better way to do this...

  public static ImageManager getInstance() {
    return ourInstance;
  }

  private ImageManager() {
    mKeywordToImageListMap = new HashMap<>();
    mKeywordToLoadedPageMap = new HashMap<>();
    mCallbackList = new ArrayList<>();
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

  private void setImageList(String keyword, List<PixabayImageObject> imageList) {
    Log.d(TAG, "Set " + keyword + " with " + ArrayUtils.getLengthSafe(imageList) + " images");

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

  ///////////////////////////// API relatives /////////////////////////////

  public void searchImage(String keyword) {
    searchImage(keyword, 1);
  }

  public void searchImage(final String keyword, final int page) {
    mSearchingKeyword = keyword;

    // TODO handle all api query together, maybe a Handler and a HandlerThread
    new Thread(new Runnable() {
      @Override
      public void run() {
        Log.d(TAG, "Search image with keyword: " + SearchUtils.formatSearchKeyword(keyword) +
            ", page: " + page);

        ApiClient.getInstance().searchImages(keyword, page, mObserver);
      }
    }).start();
  }

  public void addSearchCallback(SearchCallback searchCallback) {
    mCallbackList.add(searchCallback);
  }

  public void removeSearchCallback(SearchCallback searchCallback) {
    if (mCallbackList.contains(searchCallback)) {
      mCallbackList.remove(searchCallback);
    }
  }

  private Observer<Response<PixabayResponseObject>> mObserver =
      new Observer<Response<PixabayResponseObject>>() {

    @Override
    public void onSubscribe(Disposable d) {
      Log.d(TAG, "onSubscribe");
    }

    @Override
    public void onNext(Response<PixabayResponseObject> response) {
      if (response == null) {
        Log.e(TAG, "Response null");
        return;
      }

      if (response.isSuccessful()) {
        PixabayResponseObject object = response.body();
        if (object == null) {
          Log.e(TAG, "Response body null");
          return;
        }

        if (ArrayUtils.isNotEmpty(object.getHits())) {
          Log.d(TAG, "Received " + ArrayUtils.getLengthSafe(object.getHits()) + " images");

          setImageList(mSearchingKeyword, object.getHits());

          if (ArrayUtils.isNotEmpty(mCallbackList)) {
            for (SearchCallback callback : mCallbackList) {
              if (callback != null) {
                callback.onSuccess();
              }
            }
          }

          mSearchingKeyword = "";
        } else {
          Log.d(TAG, "Received empty image list");

          if (ArrayUtils.isNotEmpty(mCallbackList)) {
            for (SearchCallback callback : mCallbackList) {
              if (callback != null) {
                callback.onFail(R.string.no_image_found);
              }
            }
          }
        }
      } else {
        try {
          String errorMsg = response.errorBody().string();
          Log.e(TAG, "error msg: " + errorMsg);

          if (ArrayUtils.isEmpty(mCallbackList)) {
            return;
          }

          if (errorMsg.contains(Constants.FAIL_TO_CONNECT_TO_SERVER)) {
            if (ArrayUtils.isNotEmpty(mCallbackList)) {
              for (SearchCallback callback : mCallbackList) {
                if (callback != null) {
                  callback.onFail(R.string.connect_to_server_fail);
                }
              }
            }
          } else {
            if (ArrayUtils.isNotEmpty(mCallbackList)) {
              for (SearchCallback callback : mCallbackList) {
                if (callback != null) {
                  callback.onFail(R.string.general_error);
                }
              }
            }
          }
        } catch (IOException e) {
          Log.e(TAG, "Fail to get error body content");
        }
      }
    }

    @Override
    public void onError(Throwable e) {
      Log.e(TAG, e.getMessage());
    }

    @Override
    public void onComplete() {
      Log.d(TAG, "onComplete");
    }
  };

}
