package com.jajinba.pixabaydemo.network;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.jajinba.pixabaydemo.BuildConfig;
import com.jajinba.pixabaydemo.Constants;
import com.jajinba.pixabaydemo.MainApplication;
import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.model.PixabayResponseObject;
import com.jajinba.pixabaydemo.network.listener.ResponseListener;
import com.jajinba.pixabaydemo.utils.SearchUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

  private static final String API_KEY = "key";
  private static final String API_PAGE = "page";
  private static final String API_IMAGE_PER_PAGE = "per_page";
  private static final String API_KEYWORD = "q";

  // TODO should maintain a api call(request) map here if we need to cancel api request, therefore
  // I choose to use Singleton pattern here
  private static volatile ApiClient sClient;

  private ApiService mApiService;
  private Map<UUID, Call> mCallMap;

  public static ApiClient getInstance() {
    if (sClient == null) {
      synchronized (ApiClient.class) {
        if (sClient == null) {
          sClient = new ApiClient();
        }
      }
    }
    return sClient;
  }

  private ApiClient() {
    initialize();
  }

  private void initialize() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(Constants.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    mApiService = retrofit.create(ApiService.class);
  }


  private UUID addCallToMap(Call call) {
    if (call == null) {
      return null;
    }

    if (null == mCallMap) {
      mCallMap = new HashMap<>();
    }

    // TODO: Find another way to control requests.
    UUID uuid = UUID.randomUUID();

    if (mCallMap.put(uuid, call) != null) {
      return uuid;
    }

    return uuid;
  }

  private void removeCallFromMap(final UUID uuid) {
    if (null == mCallMap) {
      return;
    }

    final Call call = mCallMap.get(uuid);

    if (call != null && (!call.isCanceled() || !call.isExecuted())) {
      call.cancel();
    }
    mCallMap.remove(uuid);
  }

  public static void cancelRequestById(UUID uuid) {
    getInstance().cancelRequest(uuid);
  }

  public void cancelRequest(final UUID uuid) {
    if (mCallMap == null || uuid == null) {
      return;
    }

    Call call = mCallMap.get(uuid);
    if (call != null && !call.isCanceled() && !call.isExecuted()) {
      call.cancel();
    }
  }

  public UUID searchImages(@NonNull String keyword,
                           ResponseListener<PixabayResponseObject> responseListener) {
    return searchImages(keyword, 1, responseListener);
  }

  public UUID searchImages(@NonNull String keyword, int page,
                           ResponseListener<PixabayResponseObject> responseListener) {
    HashMap<String, String> params = new HashMap<>();
    params.put(API_KEY, BuildConfig.API_KEY);
    params.put(API_PAGE, String.valueOf(page));
    params.put(API_IMAGE_PER_PAGE, String.valueOf(Constants.IMAGE_PER_PAGE));
    params.put(API_KEYWORD, SearchUtils.formatSearchKeyword(keyword));

    return apiCall(params, responseListener);
  }

  // FIXME query with page & per_page
  private UUID apiCall(@NonNull final HashMap<String, String> filtersMap,
                       final ResponseListener<PixabayResponseObject> responseListener) {
    Call<PixabayResponseObject> call = mApiService.searchImages(filtersMap);
    final UUID uuid = addCallToMap(call);
    SimpleServiceCallback<PixabayResponseObject> callback =
        new SimpleServiceCallback<>(uuid, responseListener, "searchImages");
    call.enqueue(callback);
    return uuid;
  }

  /**
   * T1: The whole data structure returning by the API.
   * T2: The data structure that users actually need.
   */
  // TODO: In case one day we need only part of the response, so define a abstract callback class here
  private abstract class ServiceCallback<T1, T2> implements Callback<T1> {

    private static final String TAG = "H2ServiceCallback";
    private ResponseListener<T2> mListener;
    private UUID mUuid;
    private String mApiName;

    public ServiceCallback(UUID uuid, @Nullable ResponseListener<T2> listener) {
      this(uuid, listener, "");
    }

    public ServiceCallback(UUID uuid, ResponseListener<T2> listener, String apiName) {
      mUuid = uuid;
      mListener = listener;
      setApiName(apiName);
    }

    /**
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call {@link Response#isSuccessful()} to determine if the response indicates success.
     */
    @Override
    public void onResponse(Call<T1> call, Response<T1> response) {
      removeCallFromMap(mUuid);


      if (response.isSuccessful() == false || response.errorBody() != null) {
        Log.e(TAG, "onResponse; response is null.");
        if (mListener != null) {
          if (response.errorBody() != null) {
            try {
              mListener.onFailure(TextUtils.isEmpty(response.errorBody().string()) ?
                  MainApplication.getInstance().getString(R.string.general_error) :
                  response.errorBody().string());
            } catch (IOException | NullPointerException e) {
              e.printStackTrace();
            }
          } else {
            mListener.onFailure(response.toString());
          }
        }
        return;
      }

      if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
        // FIXME: add unauthorized handling
      }

      if (mListener == null) {
        return;
      }

      if (response.isSuccessful()) {
        // TODO check if body is valid
        T1 responseBody = response.body();
        if (responseBody != null) {
          T2 dataToUser = getValidData(responseBody);
          if (dataToUser != null) {
            onPreSuccess(dataToUser);
            mListener.onSuccess(dataToUser);
          } else {
            onPreSuccess(null);
            mListener.onSuccess(null);
          }
        } else {
          Log.w(TAG, "onResponse; response.body() returns null.");
          onPreSuccess(null);
          mListener.onSuccess(null);
        }

      } else {
        Log.e(TAG, String.format("onResponse, api=%s; statusCode=%d, msg=%s",
            mApiName, response.code(), response.message()));
        Log.w(TAG, "errorBody = " + response.toString());
        mListener.onFailure(response.toString());
      }
    }

    @Override
    public void onFailure(Call<T1> call, Throwable t) {
      Log.e(TAG, String.format("onFailure, api=%s", mApiName), t);

      removeCallFromMap(mUuid);

      if (mListener == null) {
        return;
      }
      // FIXME: wrap error msg for client
      mListener.onFailure(t.getMessage());
    }

    /**
     * Called before listener#onSuccess
     */
    public void onPreSuccess(@Nullable T2 object) {
    }

    public abstract T2 getValidData(@NonNull T1 object);

    public final void setApiName(String apiName) {
      mApiName = apiName;
    }
  }

  /**
   * return whole response body
   *
   * @param <T>
   */
  private class SimpleServiceCallback<T> extends ServiceCallback<T, T> {

    public SimpleServiceCallback(UUID uuid, @Nullable ResponseListener<T> listener, String apiName) {
      super(uuid, listener, apiName);
    }

    public SimpleServiceCallback(UUID uuid, @Nullable ResponseListener<T> listener) {
      super(uuid, listener);
    }

    @Override
    public T getValidData(T object) {
      return object;
    }
  }
}
