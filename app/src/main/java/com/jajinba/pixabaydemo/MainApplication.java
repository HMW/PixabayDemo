package com.jajinba.pixabaydemo;

import android.app.Application;


public class MainApplication extends Application {

  private static MainApplication sApplication;

  /**
   * Get application context instance
   *
   * @return {@link Application}
   */
  public static MainApplication getInstance() {
    return sApplication;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    sApplication = this;
  }
}
