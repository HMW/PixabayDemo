package com.example.pixabay.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.pixabay.ui.viewmodel.MainViewModel

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageList(viewModel: MainViewModel) {
  viewModel
    .searchResult
    .collectAsState(null)
    .value
    ?.imageInfoList?.let {
      LazyColumn {
        items(it) { imageInfo ->
          GlideImage(
            model = imageInfo.largeImageURL,
            contentDescription = "content description",
            modifier = Modifier.padding(5.dp)
          )
  //          {
  //            it
  //              .thumbnail(
  //                requestManager
  //                  .asDrawable()
  //                  .load(imageInfo.previewURL)
  //                  .signature(signature)
  //                  .override(THUMBNAIL_DIMENSION)
  //              )
  //              .signature(signature)
  //          }
        }
      }
  }
}
