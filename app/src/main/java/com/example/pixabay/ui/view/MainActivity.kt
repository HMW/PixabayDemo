package com.example.pixabay.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.pixabay.R
import com.example.pixabay.data.datasource.pixabay.RemotePixabayDatasource
import com.example.pixabay.data.repository.ImageRepository
import com.example.pixabay.data.repository.ImageRepositoryImpl
import com.example.pixabay.domain.usecase.ImageSearchUseCase
import com.example.pixabay.domain.usecase.ImageSearchUseCaseImpl
import com.example.pixabay.ui.viewmodel.MainViewModel
import com.example.pixabay.ui.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {

  private val remotePixabayDatasource: RemotePixabayDatasource = RemotePixabayDatasource()
  private val imageRepository: ImageRepository = ImageRepositoryImpl(remotePixabayDatasource)
  private val imageSearchUseCase: ImageSearchUseCase = ImageSearchUseCaseImpl(imageRepository)
  private val viewModel: MainViewModel by viewModels {
    MainViewModelFactory(imageSearchUseCase)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    viewModel.searchImage("Taipei")
  }

}
