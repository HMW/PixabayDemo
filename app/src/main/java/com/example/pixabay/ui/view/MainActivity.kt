package com.example.pixabay.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import com.example.pixabay.data.datasource.pixabay.RemotePixabayDatasource
import com.example.pixabay.data.repository.ImageRepository
import com.example.pixabay.data.repository.ImageRepositoryImpl
import com.example.pixabay.domain.ImageSearchUseCase
import com.example.pixabay.domain.ImageSearchUseCaseImpl
import com.example.pixabay.ui.composable.MainScreen
import com.example.pixabay.ui.viewmodel.MainViewModel
import com.example.pixabay.ui.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {

  private val remotePixabayDatasource: RemotePixabayDatasource = RemotePixabayDatasource()
  private val imageRepository: ImageRepository = ImageRepositoryImpl(remotePixabayDatasource)
  private val imageSearchUseCase: ImageSearchUseCase = ImageSearchUseCaseImpl(imageRepository)
  private val viewModel: MainViewModel by viewModels {
    MainViewModelFactory(imageSearchUseCase)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.searchImage("Taipei")
//    setContentView(
//      ComposeView(this).apply {
//        setContent {
//          MainScreen(viewModel)
//        }
//      }
//    )

    setContent {
      MaterialTheme {
        MainScreen(viewModel)
      }
    }
  }

}
