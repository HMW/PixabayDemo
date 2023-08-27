package com.example.pixabay.data.datasource.pixabay

import com.example.pixabay.data.datasource.ImageDataSource
import com.example.pixabay.data.datasource.SearchResponse
import com.example.pixabay.utils.InvalidApiKeyException
import com.example.pixabay.utils.PixabayGeneralException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException

class RemotePixabayDatasource : ImageDataSource {

  companion object {
    private const val BASE_URL = "https://pixabay.com"
    private const val API_KEY = "35471556-310c9c5ff22c0bc8755dc9f8d"
    private const val IMAGES_PER_REQUEST = 20
  }

  private val pixabayApiService = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(
      OkHttpClient.Builder()
        .addInterceptor(
          HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
          })
        .build()
    )
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(PixabayApiService::class.java)

  override suspend fun getImagesByKeyword(keyword: String): SearchResponse {
    return runCatching {
      pixabayApiService
        .getImages(API_KEY, keyword, IMAGES_PER_REQUEST)
        .execute()
        .body()
        ?.toSearchResponse() ?: throw PixabayGeneralException(message = "Get Image failed")
    }
      .onFailure {
        when(it) {
          is HttpException -> {
            when(it.code()) {
              400 -> {
                throw InvalidApiKeyException(
                  400,
                  it.response()?.errorBody()?.string() ?: it.message()
                )
              }
              else -> {
                throw PixabayGeneralException(
                  it.code(),
                  it.response()?.errorBody()?.string() ?: it.message()
                )
              }
            }
          }
          is SocketTimeoutException -> throw SocketTimeoutException(it.message)
          else -> throw it
        }
      }
      .getOrThrow()
  }

}
