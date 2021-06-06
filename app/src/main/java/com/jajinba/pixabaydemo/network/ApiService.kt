package com.jajinba.pixabaydemo.network

import com.jajinba.pixabaydemo.model.PixabayResponseObject
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.QueryMap

internal interface ApiService {
    @POST("api")
    open fun searchImages(@QueryMap params: MutableMap<String?, String?>?): Observable<Response<PixabayResponseObject?>?>?
}