package com.example.pixabay.utils

data class PixabayGeneralException(
  val code: Int? = null,
  override val message: String
) : Exception()
data class InvalidApiKeyException(
  val code: Int,
  override val message: String
) : Exception()
