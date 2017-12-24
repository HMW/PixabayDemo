package com.jajinba.pixabaydemo.network;


import com.jajinba.pixabaydemo.model.PixabayResponseObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

interface ApiService {

  @POST("api")
  Call<PixabayResponseObject> searchImages(@Query("key") String key, @Query("q") String filter);

  @POST("api")
  Call<PixabayResponseObject> searchImages(@QueryMap Map<String, String> params);

}
