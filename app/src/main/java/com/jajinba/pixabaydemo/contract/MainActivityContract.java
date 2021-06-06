package com.jajinba.pixabaydemo.contract;


import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public interface MainActivityContract {

  interface View {
    void searchStart();

    void searchFinished(boolean isSuccess, @StringRes int errorMsg);
  }

  interface Presenter {
    void search(@NonNull String keyword);
  }

}
