package com.example.pixabay.data.datasource.pixabay

import com.example.pixabay.data.datasource.ImageInfo
import com.example.pixabay.data.datasource.SearchResponse
import com.example.pixabay.utils.PixabayGeneralException
import com.google.gson.annotations.SerializedName

/**
 * Response sample
 * {
 * "total": 4692,
 * "totalHits": 500,
 * "hits": [
 *     {
 *         "id": 195893,
 *         "pageURL": "https://pixabay.com/en/blossom-bloom-flower-195893/",
 *         "type": "photo",
 *         "tags": "blossom, bloom, flower",
 *         "previewURL": "https://cdn.pixabay.com/photo/2013/10/15/09/12/flower-195893_150.jpg"
 *         "previewWidth": 150,
 *         "previewHeight": 84,
 *         "webformatURL": "https://pixabay.com/get/35bbf209e13e39d2_640.jpg",
 *         "webformatWidth": 640,
 *         "webformatHeight": 360,
 *         "largeImageURL": "https://pixabay.com/get/ed6a99fd0a76647_1280.jpg",
 *         "fullHDURL": "https://pixabay.com/get/ed6a9369fd0a76647_1920.jpg",
 *         "imageURL": "https://pixabay.com/get/ed6a9364a9fd0a76647.jpg",
 *         "imageWidth": 4000,
 *         "imageHeight": 2250,
 *         "imageSize": 4731420,
 *         "views": 7671,
 *         "downloads": 6439,
 *         "likes": 5,
 *         "comments": 2,
 *         "user_id": 48777,
 *         "user": "Josch13",
 *         "userImageURL": "https://cdn.pixabay.com/user/2013/11/05/02-10-23-764_250x250.jpg",
 *     },
 *     {
 *         "id": 73424,
 *         ...
 *     },
 *     ...
 * ]
 * }
 */
data class PixabaySearchResponse(
  val total: Int?,
  val totalHits: Int?,
  @SerializedName("hits") val imageInfoList: List<PixabayImageInfo>?
)

data class PixabayImageInfo(
  val id: Int,
  val pageURL: String,
  val type: String,
  val tags: String,
  val previewURL: String,
  val previewWidth: Int,
  val previewHeight: Int,
  val webformatURL: String,
  val webformatWidth: Int,
  val webformatHeight: Int,
  val largeImageURL: String,
  val fullHDURL: String,
  val imageWidth: Int,
  val imageHeight: Int,
  val imageSize: Int,
  val views: Int,
  val downloads: Int,
  val likes: Int,
  val comments: Int,
  @SerializedName("user_id") val userId: Int,
  val userImageURL: String
)

fun PixabaySearchResponse.toSearchResponse(): SearchResponse {
  return if (total != null && imageInfoList?.isNotEmpty() == true) {
      SearchResponse(
      true,
      total,
      imageInfoList.map { it.toImageInfo() },
        null
    )
  } else {
    throw PixabayGeneralException(message = "Response parse failed")
  }
}

fun PixabayImageInfo.toImageInfo(): ImageInfo {
  return ImageInfo(
    previewURL,
    largeImageURL
  )
}
