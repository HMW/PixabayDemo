package com.example.pixabay.domain

import com.example.pixabay.data.datasource.ErrorInfo
import com.example.pixabay.data.datasource.SearchResponse
import com.example.pixabay.data.repository.ImageRepository
import com.example.pixabay.utils.InvalidApiKeyException
import com.example.pixabay.utils.PixabayGeneralException

interface ImageSearchUseCase {
  suspend fun searchImages(keyword: String): SearchResponse
}

class ImageSearchUseCaseImpl(private val repository: ImageRepository) : ImageSearchUseCase {

  override suspend fun searchImages(keyword: String): SearchResponse = runCatching {
    repository.searchImage(keyword)
  }
    .getOrElse {
      return when(it) {
        is PixabayGeneralException -> {
          SearchResponse.buildErrorResponse(
            ErrorInfo(
              errorMsg = it.message
            )
          )
        }
        is InvalidApiKeyException -> {
          SearchResponse.buildErrorResponse(
            ErrorInfo(
              it.code,
              it.message
            )
          )
        }
        else -> {
          SearchResponse.buildErrorResponse(
            ErrorInfo(
              -1,
              it.message ?: ""
            )
          )
        }
      }
    }

}
