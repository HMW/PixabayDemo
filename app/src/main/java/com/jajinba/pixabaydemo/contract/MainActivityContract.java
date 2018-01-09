package com.jajinba.pixabaydemo.contract;


import android.support.annotation.NonNull;

public interface MainActivityContract {

  interface View {
    void searchStart();

    void searchDone();

    void showErrorDialog(String errorMsg);
  }

  interface Presenter {
    void search(@NonNull String keyword);
  }

}
