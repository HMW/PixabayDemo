package com.jajinba.pixabaydemo.contract

import androidx.annotation.StringRes

interface MainActivityContract {
  interface View {
    fun searchStart()
    fun searchFinished(isSuccess: Boolean, @StringRes errorMsg: Int)
  }

  interface Presenter {
    fun search(keyword: String)
  }
}
