package com.example.pixabay.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pixabay.data.datasource.SearchResponse
import com.example.pixabay.domain.usecase.ImageSearchUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
  private val imageSearchUseCase: ImageSearchUseCase
) : ViewModelProvider.NewInstanceFactory() {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return with(modelClass) {
      when {
        isAssignableFrom(MainViewModel::class.java) -> {
          MainViewModel(imageSearchUseCase)
        }
        else -> throw IllegalArgumentException("Unknown ViewModel (${modelClass.name}) class")
      }
    } as T
  }
}

class MainViewModel(
  private val imageSearchUseCase: ImageSearchUseCase
) : ViewModel() {

  private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    Log.d("", "vm] coroutine exception: ${exception.message}")
  }
  val searchResult: MutableStateFlow<SearchResponse?> = MutableStateFlow(null)

  fun searchImage(keyWord: String) {
    CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
      imageSearchUseCase.searchImages(keyWord).let {
        Log.d("", "vm] emit search result, ${it.isSuccess}, list size: ${it.imageInfoList.size}, error info: ${it.errorInfo}")
        searchResult.emit(it)
      }
    }
  }

}
