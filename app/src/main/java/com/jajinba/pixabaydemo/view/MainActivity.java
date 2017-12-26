package com.jajinba.pixabaydemo.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.jajinba.pixabaydemo.R;
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
    // TODO error handling, e.g., no network

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

      // hide keyboard after search
      InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
    }
  }

  public void showErrorDialogWithMsg(String errorMsg) {
    if (this.isFinishing() == false) {
      // TODO should create a custom dialog helper class
      new AlertDialog.Builder(this)
          .setMessage(errorMsg)
          .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              // do nothing
            }
          }).create().show();
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

    @Override
    public void showErrorDialog(String errorMsg) {
      showErrorDialogWithMsg(errorMsg);
    }
  };
}
