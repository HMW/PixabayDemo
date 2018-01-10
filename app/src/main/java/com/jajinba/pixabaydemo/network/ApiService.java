package com.jajinba.pixabaydemo.network;


import com.jajinba.pixabaydemo.model.PixabayResponseObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

interface ApiService {

  @POST("api")
  Observable<PixabayResponseObject> searchImages(@QueryMap Map<String, String> params);

}
