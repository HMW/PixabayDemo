package com.example.pixabay.data.repository

import com.example.pixabay.data.datasource.SearchResponse
import com.example.pixabay.data.datasource.pixabay.RemotePixabayDatasource

class ImageRepositoryImpl(
  private val remotePixabayDatasource: RemotePixabayDatasource
  // TODO request local data source
) : ImageRepository {

  override suspend fun searchImage(keyword: String): SearchResponse {
    // TODO check local data source
    return remotePixabayDatasource.getImagesByKeyword(keyword)
  }

}
