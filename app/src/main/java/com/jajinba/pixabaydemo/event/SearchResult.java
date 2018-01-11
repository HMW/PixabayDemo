package com.jajinba.pixabaydemo.event;


import android.support.annotation.StringRes;

import com.jajinba.pixabaydemo.R;

public class SearchResult {

  private boolean mIsSuccess;
  private @StringRes int mErrorMsg;

  public SearchResult(boolean isSuccess) {
    new SearchResult(isSuccess, R.string.general_error);
  }

  public SearchResult(boolean isSuccess, @StringRes int  errorMsg) {
    mIsSuccess = isSuccess;
    mErrorMsg = errorMsg;
  }

  public boolean isSuccess() {
    return mIsSuccess;
  }

  public @StringRes int getErrorMsg() {
    return mErrorMsg;
  }
}
