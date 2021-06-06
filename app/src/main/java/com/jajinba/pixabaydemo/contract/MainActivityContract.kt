package com.jajinba.pixabaydemo.contract

import androidx.annotation.StringRes

interface MainActivityContract {
    interface View {
        open fun searchStart()
        open fun searchFinished(isSuccess: Boolean, @StringRes errorMsg: Int)
    }

    interface Presenter {
        open fun search(keyword: String)
    }
}