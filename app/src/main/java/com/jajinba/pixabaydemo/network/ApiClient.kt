package com.jajinba.pixabaydemo.network

import com.jajinba.pixabaydemo.BuildConfig
import com.jajinba.pixabaydemo.Constants
import com.jajinba.pixabaydemo.model.PixabayResponseObject
import com.jajinba.pixabaydemo.utils.SearchUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ApiClient private constructor() {

  companion object {
    private const val API_KEY: String = "key"
    private const val API_PAGE: String = "page"
    private const val API_IMAGE_PER_PAGE: String = "per_page"
    private const val API_KEYWORD: String = "q"

    @Volatile
    private var sClient: ApiClient? = null
    fun getInstance(): ApiClient? {
      if (sClient == null) {
        synchronized(ApiClient::class.java) {
          if (sClient == null) {
            sClient = ApiClient()
          }
        }
      }
      return sClient
    }
  }

  private var mApiService: ApiService? = null

  init {
    initialize()
  }

  private fun initialize() {
    val retrofit = Retrofit.Builder()
      .baseUrl(Constants.API_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .build()
    mApiService = retrofit.create(ApiService::class.java)
  }

  fun searchImages(
    keyword: String, page: Int,
    subscriber: Observer<Response<PixabayResponseObject?>?>
  ) {
    val params = HashMap<String?, String?>()
    params[API_KEY] = BuildConfig.API_KEY
    params[API_PAGE] = page.toString()
    params[API_IMAGE_PER_PAGE] =
      Constants.IMAGE_PER_PAGE.toString()
    params[API_KEYWORD] = SearchUtils.formatSearchKeyword(keyword)
    apiCall(params, subscriber)
  }

  private fun apiCall(
    filtersMap: HashMap<String?, String?>,
    subscriber: Observer<Response<PixabayResponseObject?>?>
  ) {
    mApiService?.searchImages(filtersMap)
      ?.subscribeOn(Schedulers.io())
      ?.observeOn(AndroidSchedulers.mainThread())
      ?.subscribe(subscriber)
  }
}
