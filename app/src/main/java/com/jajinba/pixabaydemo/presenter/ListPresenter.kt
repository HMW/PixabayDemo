package com.jajinba.pixabaydemo.presenter

import android.text.TextUtils
import android.util.Log
import com.jajinba.pixabaydemo.contract.ListContract
import com.jajinba.pixabaydemo.model.ImageManager
import com.jajinba.pixabaydemo.model.PixabayImageObject
import com.jajinba.pixabaydemo.utils.ArrayUtils
import java.util.*

class ListPresenter(private val mView: ListContract.View) : ListContract.Presenter {

  companion object {
    private val TAG = ListPresenter::class.java.simpleName
  }

  init {
    ImageManager.getInstance().setCallback(object : ImageManager.Callback {
      override fun onSuccess(keyword: String, imageList: MutableList<PixabayImageObject>) {
        Log.d(TAG, "onSuccess, image count:" + ArrayUtils.getLengthSafe(imageList))
//        mView.searchFinished(keyword, imageList)
      }

      override fun onFailed(errorMsg: Int) {
        Log.d(TAG, "onFailed")
//        mView.onFailed(errorMsg)
      }
    })
  }

  override fun getImageList(keyword: String): MutableList<PixabayImageObject> {
    Log.d(TAG, "mvp] getImageList")
    return if (TextUtils.isEmpty(keyword)) {
      ArrayList()
    } else {
      ImageManager.getInstance().getImageListWithKeyword(keyword)
    }
  }

  override fun loadMore(keyword: String) {
    Log.d(TAG, "mvp] loadMore")
    Log.d(TAG, "Load more images of $keyword")
    val searchPage: Int = ImageManager.getInstance().previousLoadedPage(keyword) + 1
//    ImageManager.getInstance().searchImage(keyword, searchPage)
  }

  override fun getLastOperation(): String {
    Log.d(TAG, "mvp] getLastOperation")
    return ImageManager.getInstance().getLastOperation() ?: ""
  }
}
