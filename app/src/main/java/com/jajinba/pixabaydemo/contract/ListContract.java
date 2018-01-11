package com.jajinba.pixabaydemo.contract;


import com.jajinba.pixabaydemo.model.PixabayImageObject;

import java.util.List;

import io.reactivex.annotations.Nullable;

import static com.jajinba.pixabaydemo.model.ImageManager.Operation;

public interface ListContract {

  interface View {
    void searchFinished(@Nullable String keyword, @Nullable List<PixabayImageObject> imageList);
  }

  interface Presenter {
    List<PixabayImageObject> getImageList(String keyword);

    void loadMore(String keyword);

    @Operation String getLastOperation();
  }

}
