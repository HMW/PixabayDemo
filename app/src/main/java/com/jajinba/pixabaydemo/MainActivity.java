package com.jajinba.pixabaydemo;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jajinba.pixabaydemo.adapter.ViewPagerAdapter;
import com.jajinba.pixabaydemo.view.fragment.ImageGridFragment;
import com.jajinba.pixabaydemo.view.fragment.ImageListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.tablayout)
  TabLayout mTabLayout;
  @BindView(R.id.viewpager)
  ViewPager mViewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // TODO move to base activity
    ButterKnife.bind(this);

    // init view
    init();
  }

  private void init() {
    // FIXME remove after test
    List<Fragment> fragmentList = new ArrayList<>();
    fragmentList.add(ImageListFragment.newInstance());
    fragmentList.add(ImageGridFragment.newInstance());

    // setup ViewPager
    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
        fragmentList);
    mViewPager.setAdapter(viewPagerAdapter);

    // setup TabLayout
    mTabLayout.setupWithViewPager(mViewPager);
  }
}
