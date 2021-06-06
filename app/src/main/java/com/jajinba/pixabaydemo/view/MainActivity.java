package com.jajinba.pixabaydemo.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.StringRes;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.jajinba.pixabaydemo.MainApplication;
import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.adapter.ViewPagerAdapter;
import com.jajinba.pixabaydemo.contract.MainActivityContract;
import com.jajinba.pixabaydemo.presenter.MainActivityPresenter;
import com.jajinba.pixabaydemo.view.fragment.ImageGridFragment;
import com.jajinba.pixabaydemo.view.fragment.ImageListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View,
    SearchView.OnQueryTextListener {

  private static final String TAG = MainActivity.class.getSimpleName();

  @BindView(R.id.progress_bar)
  ProgressBar mProgressBar;
  @BindView(R.id.tablayout)
  TabLayout mTabLayout;
  @BindView(R.id.viewpager)
  ViewPager mViewPager;

  private MainActivityContract.Presenter mPresenter;
  private boolean isSearching = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // TODO move to base activity
    ButterKnife.bind(this);

    // init view
    initView();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options_menu, menu);

    // Associate searchable configuration with the SearchView
    SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

    // show search complete button
    searchView.setSubmitButtonEnabled(true);

    searchView.setOnQueryTextListener(this);
    return true;
  }

  private void initView() {
    // TODO error handling, e.g., no network

    mPresenter = new MainActivityPresenter(this);

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

  private void showErrorDialog(String errorMsg) {
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

  @Override
  public void searchStart() {
    isSearching = true;
    mProgressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void searchFinished(boolean isSuccess, @StringRes int errorMsg) {
    isSearching = false;
    mProgressBar.setVisibility(View.INVISIBLE);

    if (isSuccess == false) {
      showErrorDialog(MainApplication.getInstance().getString(errorMsg > 0 ?
          errorMsg : R.string.general_error));
    }
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    if (isSearching) {
      return true;
    }

    Log.d(TAG, "Submit search query");
    mPresenter.search(query);

    return true;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    // TODO
    return false;
  }
}
