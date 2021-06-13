package com.jajinba.pixabaydemo.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface ImagesSearchDataSource {
  val keyword: LiveData<String>
  val page: LiveData<Int>
  val imageList: LiveData<MutableList<PixabayImageObject>>
  val isLoading: LiveData<Boolean>
  val isLoadSuccess: LiveData<Boolean>
  val errorMsg: String
  fun loadImages(keyword: String, page: Int = 1)
}

class ImageSearchDataSourceImpl: ImagesSearchDataSource {

  private val _keyword = MutableLiveData<String>()
  private val _page = MutableLiveData<Int>()
  private val _imageList = MutableLiveData<MutableList<PixabayImageObject>>()
  private val _isLoading = MutableLiveData<Boolean>()
  private val _isLoadSuccess = MutableLiveData<Boolean>()
  private val imageManagerCallback = object: ImageManager.Callback {

    override fun onSuccess(keyword: String, imageList: MutableList<PixabayImageObject>) {
      _keyword.value = keyword
      _page.value?.let {
        _page.value = it + 1
      }
      _imageList.value = imageList
      _isLoading.value = false
      _isLoadSuccess.value = true
    }

    override fun onFailed(errorMsg: Int) {
      _isLoading.value = false
      _isLoadSuccess.value = false
      _imageList.value?.clear()
    }

  }

  init {
    ImageManager.getInstance().setCallback(imageManagerCallback)
    _keyword.value = null
    _page.value = 0
    _imageList.value = mutableListOf()
    _isLoading.value = false
    _isLoadSuccess.value = true
  }

  override val keyword: LiveData<String>
    get() = _keyword
  override val page: LiveData<Int>
    get() = _page
  override val imageList: LiveData<MutableList<PixabayImageObject>>
    get() = _imageList
  override val isLoading: LiveData<Boolean>
    get() = _isLoading
  override val isLoadSuccess: LiveData<Boolean>
    get() = _isLoadSuccess
  override val errorMsg: String = String()

  override fun loadImages(keyword: String, page: Int) {
    if (_page.value == page) {
      return
    }

    _isLoading.value = true
    _keyword.value = keyword
    ImageManager.getInstance().searchImage(keyword, page)
  }

}
