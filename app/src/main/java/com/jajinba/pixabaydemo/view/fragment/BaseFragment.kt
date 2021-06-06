package com.jajinba.pixabaydemo.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import butterknife.ButterKnife

abstract class BaseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getContentLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
    }

    /**
     * Every fragment has to inflate a layout in the onCreateView method. We have added this method to
     * avoid duplicate all the inflate code in every fragment. You only have to return the layout to
     * inflate in this method when extends BaseFragment.
     */
    @LayoutRes
    protected abstract fun getContentLayout(): Int

    /**
     * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
     * VALUE.
     *
     * @param view to extract each widget injected in the fragment.
     */
    private fun bindViews(view: View?) {
        view?.let {
            ButterKnife.bind(this, view)
        }
    }

    /**
     * If fragment needs to handle back key event, override this method
     *
     * @return false, pass to parent to handle; true, handle it in fragment.
     */
    fun onBackPressed(): Boolean {
        return false
    }

    fun isFragmentValid(): Boolean {
        return activity != null && !activity!!.isFinishing
    }
}