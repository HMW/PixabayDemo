package com.jajinba.pixabaydemo.view.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
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

import java.util.ArrayList;
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

  // cache keyword to restore image list after screen rotated
  protected String mCurrentKeyword;

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
    mRecyclerView.setAdapter(getAdapter());
    mRecyclerView.setLayoutManager(getLayoutManager());

    // restore state before rotation
    if (savedInstanceState != null) {
      // get previous keyword
      mCurrentKeyword = savedInstanceState.getString(BUNDLE_IMAGE_KEY);

      // clear instance state
      savedInstanceState.clear();

      // restore ui
      searchFinished(mCurrentKeyword, mPresenter.getImageList(mCurrentKeyword));
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

    // FIXME check is attached?
    if (isFragmentValid() == false) {
      return;
    }

    Log.d(TAG, ArrayUtils.getLengthSafe(imageList) + " image received");

    mCurrentKeyword = keyword;

    if (TextUtils.isEmpty(keyword)) {
      resetUi();
    } else {
      updateUi(imageList);
    }
  }

  /**
   * Load more request from adapter
   */
  public void loadMore() {
    mPresenter.loadMore(mCurrentKeyword);
  }

  private void resetUi() {
    getAdapter().updateList(new ArrayList<PixabayImageObject>());
    getAdapter().notifyDataSetChanged();
  }

  private void updateUi(List<PixabayImageObject> imageList) {
    mEmptyStateTextView.setVisibility(ArrayUtils.isEmpty(imageList) ? View.VISIBLE : View.GONE);
    mRecyclerView.setVisibility(ArrayUtils.isNotEmpty(imageList) ? View.VISIBLE : View.GONE);

    @Operation String lastOperation = mPresenter.getLastOperation();
    Log.d(TAG, "update ui state with operation: " + lastOperation);

    getAdapter().updateList(imageList);
    if (NEW_SEARCH.equals(lastOperation)) {
      getAdapter().notifyDataSetChanged();
    } else if (LOAD_MORE.equals(lastOperation)) {
      int oldImageCount = ArrayUtils.getLengthSafe(imageList) % Constants.IMAGE_PER_PAGE == 0 ?
          ArrayUtils.getLengthSafe(imageList) - Constants.IMAGE_PER_PAGE :
          ArrayUtils.getLengthSafe(imageList) % Constants.IMAGE_PER_PAGE;

      getAdapter().notifyItemRangeInserted(
          oldImageCount + 1,
          ArrayUtils.getLengthSafe(imageList) - oldImageCount);
    }
  }
}
