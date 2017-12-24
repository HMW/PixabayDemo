package com.jajinba.pixabaydemo.model;

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

import com.google.gson.annotations.SerializedName;

public class PixabayImageObject {

  private int id;
  @SerializedName("pageURL")
  private String pageUrl;
  private String type;
  private String tags;
  @SerializedName("previewURL")
  private String previewUrl;
  @SerializedName("previewWidth")
  private int previewWidth;
  @SerializedName("previewHeight")
  private int previewHeight;
  @SerializedName("webformatURL")
  private String webformatUrl;
  private int webformatWidth;
  private int webformatHeight;
  private int imageWidth;
  private int imageHeight;
  private int imageSize;
  private int views;
  private int downloads;
  private int favorites;
  private int likes;
  private int comments;
  @SerializedName("user_id")
  private int userId;
  @SerializedName("user")
  private String userName;
  @SerializedName("userImageURL")
  private String userImageUrl;

  public int getId() {
    return id;
  }

  public String getPageUrl() {
    return pageUrl;
  }

  public String getType() {
    return type;
  }

  public String getTags() {
    return tags;
  }

  public String getPreviewUrl() {
    return previewUrl;
  }

  public int getPreviewWidth() {
    return previewWidth;
  }

  public int getPreviewHeight() {
    return previewHeight;
  }

  public String getWebformatUrl() {
    return webformatUrl;
  }

  public int getWebformatWidth() {
    return webformatWidth;
  }

  public int getWebformatHeight() {
    return webformatHeight;
  }

  public int getImageWidth() {
    return imageWidth;
  }

  public int getImageHeight() {
    return imageHeight;
  }

  public int getImageSize() {
    return imageSize;
  }

  public int getViews() {
    return views;
  }

  public int getDownloads() {
    return downloads;
  }

  public int getFavorites() {
    return favorites;
  }

  public int getLikes() {
    return likes;
  }

  public int getComments() {
    return comments;
  }

  public int getUserId() {
    return userId;
  }

  public String getUserName() {
    return userName;
  }

  public String getUserImageUrl() {
    return userImageUrl;
  }
}
