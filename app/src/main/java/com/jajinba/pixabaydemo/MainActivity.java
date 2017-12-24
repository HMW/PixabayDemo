package com.jajinba.pixabaydemo;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.jajinba.pixabaydemo.network.ApiClient;
import com.jajinba.pixabaydemo.network.listener.ResponseListener;
import com.jajinba.pixabaydemo.adapter.ViewPagerAdapter;
import com.jajinba.pixabaydemo.view.fragment.DummyFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

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

    // FIXME remove after test
    new Thread(new Runnable() {
      @Override
      public void run() {
        HashMap<String, String> filterMap = new HashMap<>();
        filterMap.put("key", "key");// FIXME replace with key
        filterMap.put("q", "yellow+flower");

        ApiClient.getInstance().getImages(filterMap, responseListener);
      }
    }).start();
  }

  private void init() {
    // FIXME remove after test
    List<Fragment> fragmentList = new ArrayList<>();
    fragmentList.add(new DummyFragment());
    fragmentList.add(new DummyFragment());

    // setup ViewPager
    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
        fragmentList);
    mViewPager.setAdapter(viewPagerAdapter);

    // setup TabLayout
    mTabLayout.setupWithViewPager(mViewPager);
  }

  private ResponseListener responseListener = new ResponseListener() {
    @Override
    public void onSuccess(@Nullable Object object) {
      if (object != null) {
        Log.e(TAG, object.toString());
      }
    }

    @Override
    public void onFailure(String errorMsg) {
      Log.e(TAG, "error msg: " + errorMsg);
    }
  };
}
