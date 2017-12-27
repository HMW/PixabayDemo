package com.jajinba.pixabaydemo.utils;


public class SearchUtils {

  public static String formatSearchKeyword(String keyword) {
    return keyword.contains(" ") ? keyword.replace(" ", "+") : keyword;
  }

}
