package com.jajinba.pixabaydemo.presenter

import androidx.annotation.StringRes
import com.jajinba.pixabaydemo.contract.MainActivityContract
import com.jajinba.pixabaydemo.model.ImageManager
import com.jajinba.pixabaydemo.model.PixabayImageObject

class MainActivityPresenter(private val mView: MainActivityContract.View) :
  MainActivityContract.Presenter {
  override fun search(keyword: String) {
    mView.searchStart()
    ImageManager.getInstance().searchImage(keyword)
  }

  init {
    ImageManager.getInstance().setCallback(object : ImageManager.Callback {
      override fun onSuccess(keyword: String, imageList: MutableList<PixabayImageObject>) {
        mView.searchFinished(true, -1)
      }

      override fun onFailed(@StringRes errorMsg: Int) {
        mView.searchFinished(false, errorMsg)
      }
    })
  }
}
