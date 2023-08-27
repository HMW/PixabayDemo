package com.example.pixabay.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pixabay.R
import com.example.pixabay.databinding.FragmentMainBinding
import com.example.pixabay.ui.adapter.ItemAdapter
import com.example.pixabay.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainFragment : Fragment(R.layout.fragment_main) {

  private var _binding: FragmentMainBinding? = null

  private val viewModel: MainViewModel by activityViewModels()

  // This property is only valid between onCreateView and onDestroyView.
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentMainBinding.inflate(inflater, container, false)
    binding.mbSearch.setOnClickListener {
      viewModel.searchImage(binding.mactvInput.text.toString())
    }
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewLifecycleOwner.lifecycleScope.launch {
      lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.searchResult.collect {
          Log.d("", "f] search result update, is success ${it?.isSuccess}")
          if (it?.isSuccess == true) {
            it.imageInfoList.let { imageList ->
              binding.rvImageList.adapter = ItemAdapter(imageList)
            }
          } else {
            // TODO show error UI
            Toast.makeText(context, it?.errorInfo?.errorMsg, Toast.LENGTH_LONG).show()
          }
        }
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}
