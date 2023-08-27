package com.example.pixabay.data.repository

import com.example.pixabay.data.datasource.SearchResponse


interface ImageRepository {
  suspend fun searchImage(keyword: String): SearchResponse
}
