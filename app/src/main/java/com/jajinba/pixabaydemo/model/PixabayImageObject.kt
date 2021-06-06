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
 */   class PixabayImageObject {
    private val id = 0

    @SerializedName("pageURL")
    private val pageUrl: String? = null
    private val type: String? = null
    private val tags: String? = null

    @SerializedName("previewURL")
    private val previewUrl: String? = null

    @SerializedName("previewWidth")
    private val previewWidth = 0

    @SerializedName("previewHeight")
    private val previewHeight = 0

    @SerializedName("webformatURL")
    private val webformatUrl: String? = null
    private val webformatWidth = 0
    private val webformatHeight = 0
    private val imageWidth = 0
    private val imageHeight = 0
    private val imageSize = 0
    private val views = 0
    private val downloads = 0
    private val favorites = 0
    private val likes = 0
    private val comments = 0

    @SerializedName("user_id")
    private val userId = 0

    @SerializedName("user")
    private val userName: String? = null

    @SerializedName("userImageURL")
    private val userImageUrl: String? = null
    fun getId(): Int {
        return id
    }

    fun getPageUrl(): String? {
        return pageUrl
    }

    fun getType(): String? {
        return type
    }

    fun getTags(): String? {
        return tags
    }

    fun getPreviewUrl(): String? {
        return previewUrl
    }

    fun getPreviewWidth(): Int {
        return previewWidth
    }

    fun getPreviewHeight(): Int {
        return previewHeight
    }

    fun getWebformatUrl(): String? {
        return webformatUrl
    }

    fun getWebformatWidth(): Int {
        return webformatWidth
    }

    fun getWebformatHeight(): Int {
        return webformatHeight
    }

    fun getImageWidth(): Int {
        return imageWidth
    }

    fun getImageHeight(): Int {
        return imageHeight
    }

    fun getImageSize(): Int {
        return imageSize
    }

    fun getViews(): Int {
        return views
    }

    fun getDownloads(): Int {
        return downloads
    }

    fun getFavorites(): Int {
        return favorites
    }

    fun getLikes(): Int {
        return likes
    }

    fun getComments(): Int {
        return comments
    }

    fun getUserId(): Int {
        return userId
    }

    fun getUserName(): String? {
        return userName
    }

    fun getUserImageUrl(): String? {
        return userImageUrl
    }
}