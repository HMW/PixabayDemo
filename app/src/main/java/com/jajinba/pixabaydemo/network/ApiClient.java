package com.jajinba.pixabaydemo.network;


import android.support.annotation.NonNull;

import com.jajinba.pixabaydemo.BuildConfig;
import com.jajinba.pixabaydemo.Constants;
import com.jajinba.pixabaydemo.model.PixabayResponseObject;
import com.jajinba.pixabaydemo.utils.SearchUtils;

import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

  private static final String API_KEY = "key";
  private static final String API_PAGE = "page";
  private static final String API_IMAGE_PER_PAGE = "per_page";
  private static final String API_KEYWORD = "q";

  // TODO should maintain a api call(request) map here if we need to cancel api request, therefore
  // I choose to use Singleton pattern here
  private static volatile ApiClient sClient;

  private ApiService mApiService;

  public static ApiClient getInstance() {
    if (sClient == null) {
      synchronized (ApiClient.class) {
        if (sClient == null) {
          sClient = new ApiClient();
        }
      }
    }
    return sClient;
  }

  private ApiClient() {
    initialize();
  }

  private void initialize() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(Constants.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();

    mApiService = retrofit.create(ApiService.class);
  }

  public void searchImages(@NonNull String keyword, int page,
                           Observer<Response<PixabayResponseObject>> subscriber) {
    HashMap<String, String> params = new HashMap<>();
    params.put(API_KEY, BuildConfig.API_KEY);
    params.put(API_PAGE, String.valueOf(page));
    params.put(API_IMAGE_PER_PAGE, String.valueOf(Constants.IMAGE_PER_PAGE));
    params.put(API_KEYWORD, SearchUtils.formatSearchKeyword(keyword));

    apiCall(params, subscriber);
  }

  private void apiCall(@NonNull HashMap<String, String> filtersMap,
                       Observer<Response<PixabayResponseObject>> subscriber) {
    mApiService.searchImages(filtersMap)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);
  }
}
