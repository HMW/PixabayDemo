package com.jajinba.pixabaydemo.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jajinba.pixabaydemo.Constants;
import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.adapter.ImageListAdapter;
import com.jajinba.pixabaydemo.contract.ListContract;
import com.jajinba.pixabaydemo.model.PixabayImageObject;
import com.jajinba.pixabaydemo.presenter.ListPresenter;
import com.jajinba.pixabaydemo.utils.ArrayUtils;

import java.util.List;

import butterknife.BindView;

import static com.jajinba.pixabaydemo.model.ImageManager.Operation;
import static com.jajinba.pixabaydemo.model.ImageManager.LOAD_MORE;
import static com.jajinba.pixabaydemo.model.ImageManager.NEW_SEARCH;

public abstract class ListFragment extends BaseFragment implements ListContract.View {

  private static final String TAG = ListFragment.class.getSimpleName();

  private static final String BUNDLE_IMAGE_KEY = "bundle.image.key";

  @BindView(R.id.empty_state_tv)
  TextView mEmptyStateTextView;
  @BindView(R.id.recycler_view)
  RecyclerView mRecyclerView;

  private ListContract.Presenter mPresenter;

  protected String mCurrentKeyword;
  protected List<PixabayImageObject> mImageList;
  private boolean mIsLoading = false;

  protected abstract ImageListAdapter getAdapter();

  protected abstract RecyclerView.LayoutManager getLayoutManager();

  @Override
  protected int getContentLayout() {
    return R.layout.fragment_list;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    if (mPresenter == null) {
      mPresenter = new ListPresenter(this);
    }

    // restore state before rotation
    if (savedInstanceState != null) {
      mCurrentKeyword = savedInstanceState.getString(BUNDLE_IMAGE_KEY);
      savedInstanceState.clear();

      mImageList = mPresenter.getImageList(mCurrentKeyword);

      updateUiState();
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(BUNDLE_IMAGE_KEY, mCurrentKeyword);
  }

  @Override
  public void searchFinished(String keyword, List<PixabayImageObject> imageList) {
    Log.d(TAG, "Receive search finished callback");

    getAdapter().searchFinished();

    if (TextUtils.isEmpty(keyword)) {
      resetUi();
    } else {
      mCurrentKeyword = keyword;
      mImageList = imageList;

      // FIXME check is attached?
      if (isFragmentValid()) {
        int imageCount = ArrayUtils.getLengthSafe(mImageList);
        Log.d(TAG, imageCount + " image received");

        updateUiState();
      }
    }
  }

  /**
   * Load more request from adapter (button clicked)
   */
  // FIXME should define in interface
  public void loadMore() {
    if (mIsLoading == false) {
      mPresenter.loadMore(mCurrentKeyword);
      mIsLoading = true;
    }
  }

  private void resetUi() {
    getAdapter().notifyItemChanged(ArrayUtils.getLengthSafe(mImageList));

    if (mIsLoading) {
      mIsLoading = false;
    }
  }

  private void updateUiState() {
    mEmptyStateTextView.setVisibility(ArrayUtils.isNotEmpty(mImageList) ? View.GONE : View.VISIBLE);
    mRecyclerView.setVisibility(ArrayUtils.isNotEmpty(mImageList) ? View.VISIBLE : View.GONE);

    mRecyclerView.setAdapter(getAdapter());
    mRecyclerView.setLayoutManager(getLayoutManager());
    //mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    mRecyclerView.setItemAnimator(null);

    @Operation String lastOperation = mPresenter.getLastOperation();
    Log.d(TAG, "update ui state with operation: " + lastOperation);

    if (NEW_SEARCH.equals(lastOperation)) {
      getAdapter().updateList(mImageList);
      //getAdapter().notifyDataSetChanged();
    } else if (LOAD_MORE.equals(lastOperation)) {
      // FIXME should get actual number
      int newImageCount = ArrayUtils.getLengthSafe(mImageList) % Constants.IMAGE_PER_PAGE == 0 ?
          ArrayUtils.getLengthSafe(mImageList) - Constants.IMAGE_PER_PAGE :
          ArrayUtils.getLengthSafe(mImageList) % Constants.IMAGE_PER_PAGE;

      // FIXME not scroll to new image list smoothly...
      getAdapter().updateList(mImageList);
      //getAdapter().notifyItemRangeInserted(ArrayUtils.getLengthSafe(mImageList) - newImageCount + 1,
      //    newImageCount);
      //mRecyclerView.scrollToPosition(ArrayUtils.getLengthSafe(mImageList) - newImageCount + 1);
    }
  }
}
