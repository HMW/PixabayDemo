package com.jajinba.pixabaydemo.view.fragment;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
    return inflater.inflate(getContentLayout(), container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    bindViews(view);
  }

  @Override
  public void onStart() {
    super.onStart();
  }

  /**
   * Every fragment has to inflate a layout in the onCreateView method. We have added this method to
   * avoid duplicate all the inflate code in every fragment. You only have to return the layout to
   * inflate in this method when extends BaseFragment.
   */
  protected abstract
  @LayoutRes
  int getContentLayout();

  /**
   * Replace every field annotated with ButterKnife annotations like @InjectView with the proper
   * VALUE.
   *
   * @param view to extract each widget injected in the fragment.
   */
  private void bindViews(final View view) {
    ButterKnife.bind(this, view);
  }

  /**
   * If fragment needs to handle back key event, override this method
   *
   * @return false, pass to parent to handle; true, handle it in fragment.
   */
  public boolean onBackPressed() {
    return false;
  }

  public boolean isFragmentValid() {
    return getActivity() != null && !getActivity().isFinishing();
  }
}
