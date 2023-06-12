package com.example.pixabay.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pixabay.ui.viewmodel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel) {
  var text by remember { mutableStateOf("") }

  Column {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(5.dp)
    ) {
      OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Enter to search") },
        modifier = Modifier.height(65.dp)
      )
      Spacer(modifier = Modifier.width(5.dp))
      Button(
        onClick = { viewModel.searchImage(text) },
        modifier = Modifier.height(65.dp)
      ) {
        Text("Go")
      }
    }
    ImageList(viewModel = viewModel)
  }
}
