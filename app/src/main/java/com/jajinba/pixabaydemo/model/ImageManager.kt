package com.jajinba.pixabaydemo.model

import android.text.TextUtils
import android.util.Log
import androidx.annotation.StringDef
import androidx.annotation.StringRes
import com.jajinba.pixabaydemo.Constants
import com.jajinba.pixabaydemo.R
import com.jajinba.pixabaydemo.network.ApiClient
import com.jajinba.pixabaydemo.utils.ArrayUtils
import com.jajinba.pixabaydemo.utils.SearchUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.Response
import java.io.IOException
import java.util.*

class ImageManager private constructor() {
  interface Callback {
    fun onSuccess(keyword: String, imageList: MutableList<PixabayImageObject>)
    fun onFailed(@StringRes errorMsg: Int)
  }

  companion object {
    private val TAG = ImageManager::class.java.simpleName
    private val ourInstance: ImageManager = ImageManager()
    const val NEW_SEARCH: String = "new.search"
    const val LOAD_MORE: String = "load.more"
    fun getInstance(): ImageManager {
      return ourInstance
    }
  }


  @StringDef(NEW_SEARCH, LOAD_MORE)
  annotation class Operation

  @Operation
  private var mLastOperation: String? = null
  private var mSearchingKeyword: String? = null
  private var mKeywordToImageListMap = mutableMapOf<String, MutableList<PixabayImageObject>>()
  private val mKeywordToLoadedPageMap = mutableMapOf<String, Int>()
  private val mCallbackList = mutableListOf<Callback>()

  @Operation
  fun getLastOperation(): String? {
    return mLastOperation
  }

  fun hasKeywordSearchBefore(keyword: String): Boolean {
    return mKeywordToImageListMap.containsKey(keyword)
  }

  fun previousLoadedPage(keyword: String): Int {
    return if (mKeywordToLoadedPageMap.containsKey(keyword).not()) {
      0
    } else mKeywordToLoadedPageMap[keyword] ?: 0
  }

  fun getImageListWithKeyword(keyword: String): MutableList<PixabayImageObject> {
    return if (TextUtils.isEmpty(keyword) || mKeywordToImageListMap.containsKey(keyword)
        .not()
    ) {
      ArrayList()
    } else ArrayList(
      mKeywordToImageListMap[keyword]
    )
  }

  private fun setImageList(keyword: String, imageList: MutableList<PixabayImageObject>) {
    Log.d(TAG, "Set " + keyword + " with " + ArrayUtils.getLengthSafe(imageList) + " images")

    // update image list
    if (mKeywordToImageListMap.containsKey(keyword)) {
      // put new list to previous list to keep image order
      mKeywordToImageListMap[keyword]?.addAll(imageList)
      mLastOperation = LOAD_MORE
    } else if (ArrayUtils.isNotEmpty(imageList)) {
      mKeywordToImageListMap[keyword] = imageList
      mLastOperation = NEW_SEARCH
    }

    // update loaded page
    if (mKeywordToLoadedPageMap.containsKey(keyword)) {
      mKeywordToLoadedPageMap[keyword] = mKeywordToLoadedPageMap[keyword]!!.plus(1)
    } else {
      mKeywordToLoadedPageMap[keyword] = 1
    }
    setCurrentKeyword(keyword)
  }

  fun setCallback(callback: Callback) {
    mCallbackList.add(callback)
  }

  private fun setCurrentKeyword(keyword: String) {
    if (TextUtils.isEmpty(keyword).not() && ArrayUtils.isNotEmpty(mCallbackList)) {
      for (callback in mCallbackList) {
        callback.onSuccess(keyword, ArrayList(mKeywordToImageListMap[keyword]))
      }
    }
  }

  ///////////////////////////// API relatives /////////////////////////////
  @JvmOverloads
  fun searchImage(keyword: String, page: Int = 1) {
    Log.d(TAG, "searchImage, $keyword - $page")
    if (hasKeywordSearchBefore(keyword) && mKeywordToLoadedPageMap[keyword] == page) {
      Log.d(TAG, "Keyword $keyword has searched before, use previous list instead")
      setCurrentKeyword(keyword)
    } else {
      mSearchingKeyword = keyword
      Log.d(
        TAG, "Search image with keyword: " + SearchUtils.formatSearchKeyword(keyword) +
            ", page: " + page
      )
      ApiClient.getInstance()?.searchImages(keyword, page, mObserver)
    }
  }

  private val mObserver: Observer<Response<PixabayResponseObject?>?> =
    object : Observer<Response<PixabayResponseObject?>?> {

      override fun onComplete() {
        Log.d(TAG, "onComplete")
      }

      override fun onSubscribe(d: Disposable) {
        Log.d(TAG, "onSubscribe")
      }

      override fun onNext(response: Response<PixabayResponseObject?>) {
        try {
          if (response.errorBody() != null &&
            response.errorBody()!!.string().contains(Constants.PAGE_OUT_OR_RANGE)
          ) {
            // FIXME should define a callback to notify this response and refresh UI
            return
          }
        } catch (e: IOException) {
          e.message?.let { Log.e(TAG, it) }
        }
        if (response.isSuccessful) {
          val `object` = response.body()
          if (`object` == null) {
            Log.e(TAG, "Response body null")
            return
          }
          val imageList = `object`.hits
          if (ArrayUtils.isNotEmpty(imageList)) {
            Log.d(TAG, "Received " + ArrayUtils.getLengthSafe(imageList) + " images")
            if (null != mSearchingKeyword && null != imageList) {
              setImageList(mSearchingKeyword!!, imageList)
            }
            mSearchingKeyword = ""
          } else {
            Log.d(TAG, "Received empty image list")
            if (ArrayUtils.isNotEmpty(mCallbackList)) {
              for (callback in mCallbackList) {
                callback.onFailed(R.string.no_image_found)
              }
            }
          }
        } else {
          try {
            Log.e(TAG, "error msg: ${response.errorBody()?.string()}")
            if (ArrayUtils.isNotEmpty(mCallbackList)) {
              for (callback in mCallbackList) {
                callback.onFailed(R.string.general_error)
              }
            }
          } catch (e: IOException) {
            Log.e(TAG, "Fail to get error body content")
          }
        }
      }

      override fun onError(e: Throwable) {
        e.message?.let {
          Log.e(TAG, it)
          if (ArrayUtils.isNotEmpty(mCallbackList)) {
            for (callback in mCallbackList) {
              callback.onFailed(
                if (it.contains(Constants.FAIL_TO_CONNECT_TO_SERVER)) {
                  R.string.connect_to_server_fail
                } else {
                  R.string.general_error
                }
              )
            }
          }
        }
      }
    }
}
