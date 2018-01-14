package com.jajinba.pixabaydemo.model;


import android.support.annotation.StringDef;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.network.ApiClient;
import com.jajinba.pixabaydemo.utils.ArrayUtils;
import com.jajinba.pixabaydemo.utils.SearchUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

import static com.jajinba.pixabaydemo.Constants.FAIL_TO_CONNECT_TO_SERVER;
import static com.jajinba.pixabaydemo.Constants.PAGE_OUT_OR_RANGE;

public class ImageManager {
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

  public interface Callback {
    void onSuccess(String keyword, List<PixabayImageObject> imageList);
    void onFailed(@StringRes int errorMsg);
  }

  private
  @ImageManager.Operation
  String mLastOperation;
  private String mSearchingKeyword;// FIXME better naming
  private Map<String, List<PixabayImageObject>> mKeywordToImageListMap;
  private Map<String, Integer> mKeywordToLoadedPageMap;
  private List<Callback> mCallbackList;

  public static ImageManager getInstance() {
    return ourInstance;
  }

  private ImageManager() {
    mKeywordToImageListMap = new HashMap<>();
    mKeywordToLoadedPageMap = new HashMap<>();
    mCallbackList = new ArrayList<>();
  }

  public
  @Operation
  String getLastOperation() {
    return mLastOperation;
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

    return new ArrayList<>(mKeywordToImageListMap.get(keyword));
  }

  private void setImageList(String keyword, List<PixabayImageObject> imageList) {
    Log.d(TAG, "Set " + keyword + " with " + ArrayUtils.getLengthSafe(imageList) + " images");

    // update image list
    if (mKeywordToImageListMap.containsKey(keyword)) {
      // put new list to previous list to keep image order
      mKeywordToImageListMap.get(keyword).addAll(imageList);
      mLastOperation = LOAD_MORE;
    } else if (ArrayUtils.isNotEmpty(imageList)) {
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

  public void setCallback(Callback callback) {
    mCallbackList.add(callback);
  }

  private void setCurrentKeyword(String keyword) {
    if (TextUtils.isEmpty(keyword) == false && ArrayUtils.isNotEmpty(mCallbackList)) {
      for (Callback callback : mCallbackList) {
        callback.onSuccess(keyword, new ArrayList<>(mKeywordToImageListMap.get(keyword)));
      }
    }
  }

  ///////////////////////////// API relatives /////////////////////////////

  public void searchImage(String keyword) {
    searchImage(keyword, 1);
  }

  public void searchImage(final String keyword, final int page) {
    if (hasKeywordSearchBefore(keyword) && mKeywordToLoadedPageMap.get(keyword) == page) {
      Log.d(TAG, "Keyword " + keyword + " has searched before, use previous list instead");
      setCurrentKeyword(keyword);
    } else {
      mSearchingKeyword = keyword;
      Log.d(TAG, "Search image with keyword: " + SearchUtils.formatSearchKeyword(keyword) +
          ", page: " + page);

      ApiClient.getInstance().searchImages(keyword, page, mObserver);
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

      try {
        if (response.errorBody() != null &&
            response.errorBody().string().contains(PAGE_OUT_OR_RANGE)) {
          // FIXME should define a callback to notify this response and refresh UI
          return;
        }
      } catch (IOException e) {
        Log.e(TAG, e.getMessage());
      }

      if (response.isSuccessful()) {
        PixabayResponseObject object = response.body();
        if (object == null) {
          Log.e(TAG, "Response body null");
          return;
        }

        List<PixabayImageObject> imageList = object.getHits();
        if (ArrayUtils.isNotEmpty(imageList)) {
          Log.d(TAG, "Received " + ArrayUtils.getLengthSafe(imageList) + " images");

          setImageList(mSearchingKeyword, imageList);
          mSearchingKeyword = "";
        } else {
          Log.d(TAG, "Received empty image list");

          if (ArrayUtils.isNotEmpty(mCallbackList)) {
            for (Callback callback : mCallbackList) {
              callback.onFailed(R.string.no_image_found);
            }
          }
        }
      } else {
        try {
          String errorMsg = response.errorBody().string();
          Log.e(TAG, "error msg: " + errorMsg);

          if (ArrayUtils.isNotEmpty(mCallbackList)) {
            for (Callback callback : mCallbackList) {
              callback.onFailed(R.string.general_error);
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

      if (ArrayUtils.isNotEmpty(mCallbackList)) {
        for (Callback callback : mCallbackList) {
          callback.onFailed(e.getMessage().contains(FAIL_TO_CONNECT_TO_SERVER) ?
              R.string.connect_to_server_fail : R.string.general_error);
        }
      }
    }

    @Override
    public void onComplete() {
      Log.d(TAG, "onComplete");
    }
  };
}
