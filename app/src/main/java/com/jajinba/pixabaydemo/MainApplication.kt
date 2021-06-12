package com.jajinba.pixabaydemo

import android.app.Application

class MainApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    sApplication = this
  }

  companion object {
    private var sApplication: MainApplication? = null

    /**
     * Get application context instance
     *
     * @return [Application]
     */
    fun getInstance(): MainApplication? {
      return sApplication
    }
  }
}
