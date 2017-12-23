package com.jajinba.pixabaydemo.view.fragment;


import com.jajinba.pixabaydemo.R;

public class DummyFragment extends BaseFragment {

  public DummyFragment newInstance() {
    return new DummyFragment();
  }

  @Override
  protected int getContentLayout() {
    return R.layout.fragment_dummy;
  }
}
