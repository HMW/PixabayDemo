package com.example.pixabay.data.datasource

import com.example.pixabay.data.datasource.SearchResponse.Companion.DEFAULT_ERROR_CODE

data class SearchResponse(
  val isSuccess: Boolean,
  val total: Int,
  val imageInfoList: List<ImageInfo>,
  val errorInfo: ErrorInfo?
) {
  companion object {
    private const val DEFAULT_TOTAL_COUNT = -1
    const val DEFAULT_ERROR_CODE = -1
    fun buildErrorResponse(errorInfo: ErrorInfo): SearchResponse {
      return SearchResponse(
        false,
        DEFAULT_TOTAL_COUNT,
        mutableListOf(),
        errorInfo
      )
    }
  }
}

data class ImageInfo(
  val previewURL: String,
  val largeImageURL: String
)

data class ErrorInfo(
  val errorCode: Int = DEFAULT_ERROR_CODE,
  val errorMsg: String
)
