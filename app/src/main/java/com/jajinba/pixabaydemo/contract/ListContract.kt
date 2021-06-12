package com.jajinba.pixabaydemo.contract

import androidx.annotation.StringRes
import com.jajinba.pixabaydemo.model.ImageManager
import com.jajinba.pixabaydemo.model.PixabayImageObject

interface ListContract {
    interface View {
        fun searchFinished(keyword: String, imageList: MutableList<PixabayImageObject>)
        fun onFailed(@StringRes errorMsg: Int)
    }

    interface Presenter {
        fun getImageList(keyword: String): MutableList<PixabayImageObject>
        fun loadMore(keyword: String)

        @ImageManager.Operation
        fun getLastOperation(): String
    }
}
