package com.example.pixabay.data.datasource

interface ImageDataSource {
  suspend fun getImagesByKeyword(keyword: String): SearchResponse
}
