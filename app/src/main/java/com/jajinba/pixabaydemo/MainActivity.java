package com.jajinba.pixabaydemo;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.jajinba.pixabaydemo.adapter.ViewPagerAdapter;
import com.jajinba.pixabaydemo.presenter.MainActivityPresenter;
import com.jajinba.pixabaydemo.view.fragment.ImageGridFragment;
import com.jajinba.pixabaydemo.view.fragment.ImageListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.search_et)
  EditText mSearchEditText;
  @BindView(R.id.progress_bar)
  ProgressBar mProgressBar;
  @BindView(R.id.tablayout)
  TabLayout mTabLayout;
  @BindView(R.id.viewpager)
  ViewPager mViewPager;

  private MainActivityPresenter mPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // TODO move to base activity
    ButterKnife.bind(this);

    // init view
    initView();
  }

  private void initView() {
    mPresenter = new MainActivityPresenter(mCallback);

    List<Fragment> fragmentList = new ArrayList<>();
    fragmentList.add(ImageListFragment.newInstance());
    fragmentList.add(ImageGridFragment.newInstance());

    // setup ViewPager
    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this,
        fragmentList);
    mViewPager.setAdapter(viewPagerAdapter);

    // setup TabLayout
    mTabLayout.setupWithViewPager(mViewPager);
  }

  @OnClick(R.id.search_btn)
  public void search() {
    if (TextUtils.isEmpty(mSearchEditText.getText().toString()) == false) {
      mPresenter.onSearchClick(mSearchEditText.getText().toString());
    }
  }

  private MainActivityPresenter.Callback mCallback = new MainActivityPresenter.Callback() {
    @Override
    public void searchStart() {
      mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void searchDone() {
      mProgressBar.setVisibility(View.INVISIBLE);
    }
  };
}
