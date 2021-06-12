package com.jajinba.pixabaydemo.view.fragment

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class BaseFragment(@LayoutRes layoutResource: Int) : Fragment(layoutResource) {

  /**
   * If fragment needs to handle back key event, override this method
   *
   * @return false, pass to parent to handle; true, handle it in fragment.
   */
  fun onBackPressed(): Boolean {
    return false
  }

  fun isFragmentValid(): Boolean {
    return isRemoving.not()
        && activity != null
        && requireActivity().isFinishing.not()
  }
}
