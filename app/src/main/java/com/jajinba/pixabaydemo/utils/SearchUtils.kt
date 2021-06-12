package com.jajinba.pixabaydemo.utils

object SearchUtils {

  fun formatSearchKeyword(keyword: String): String {
    return if (keyword.contains(" ")) {
      keyword.replace(" ", "+")
    } else keyword
  }

}
