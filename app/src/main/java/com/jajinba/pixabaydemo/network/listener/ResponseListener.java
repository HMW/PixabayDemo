package com.jajinba.pixabaydemo.network.listener;

import android.support.annotation.Nullable;

/**
 * response callback interface for H2 API client
 */

public interface ResponseListener<T> {
  void onSuccess(@Nullable T object);

  void onFailure(String errorMsg);
  // TODO Should define a helper class(ResponseError) to parse response error
}
