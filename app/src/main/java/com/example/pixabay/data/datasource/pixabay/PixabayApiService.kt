package com.example.pixabay.data.datasource.pixabay

import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {

  @GET("/api/")
  suspend fun getImages(
    @Query("key") key: String,
    @Query("q") keyword: String,
    @Query("per_page") perPage: Int
  ): PixabaySearchResponse

}
