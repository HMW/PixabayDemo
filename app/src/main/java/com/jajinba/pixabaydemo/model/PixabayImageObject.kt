package com.jajinba.pixabaydemo.model

import com.google.gson.annotations.SerializedName

/*
{
    "id": 195893,
    "pageURL": "https://pixabay.com/en/blossom-bloom-flower-yellow-close-195893/",
    "type": "photo",
    "tags": "blossom, bloom, flower",
    "previewURL": "https://static.pixabay.com/photo/2013/10/15/09/12/flower-195893_150.jpg"
    "previewWidth": 150,
    "previewHeight": 84,
    "webformatURL": "https://pixabay.com/get/35bbf209db8dc9f2fa36746403097ae226b796b9e13e39d2_640.jpg",
    "webformatWidth": 640,
    "webformatHeight": 360,
    "imageWidth": 4000,
    "imageHeight": 2250,
    "imageSize": 4731420,
    "views": 7671,
    "downloads": 6439,
    "favorites": 1,
    "likes": 5,
    "comments": 2,
    "user_id": 48777,
    "user": "Josch13",
    "userImageURL": "https://static.pixabay.com/user/2013/11/05/02-10-23-764_250x250.jpg",
}
 */
data class PixabayImageObject(
    val id: Int = 0,

    @SerializedName("pageURL")
    val pageUrl: String? = null,
    val type: String? = null,
    val tags: String? = null,

    @SerializedName("previewURL")
    val previewUrl: String? = null,

    @SerializedName("previewWidth")
    val previewWidth: Int = 0,

    @SerializedName("previewHeight")
    val previewHeight: Int = 0,

    @SerializedName("webformatURL")
    val webformatUrl: String? = null,
    val webformatWidth: Int = 0,
    val webformatHeight: Int = 0,
    val imageWidth: Int = 0,
    val imageHeight: Int = 0,
    val imageSize: Int = 0,
    val views: Int = 0,
    val downloads: Int = 0,
    val favorites: Int = 0,
    val likes: Int = 0,
    val comments: Int = 0,

    @SerializedName("user_id")
    val userId: Int = 0,

    @SerializedName("user")
    val userName: String? = null,

    @SerializedName("userImageURL")
    val userImageUrl: String? = null
)
