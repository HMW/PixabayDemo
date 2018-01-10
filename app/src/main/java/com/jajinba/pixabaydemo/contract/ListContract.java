package com.jajinba.pixabaydemo.contract;


import com.jajinba.pixabaydemo.model.PixabayImageObject;

import java.util.List;

import static com.jajinba.pixabaydemo.model.ImageManager.Operation;

public interface ListContract {

  interface View {
    void updateImageList(String keyword, List<PixabayImageObject> imageList);

    void showErrorMsgDialog(String errorMsg);

    void searchFinished();
  }

  interface Presenter {
    List<PixabayImageObject> getImageList(String keyword);

    void loadMore(String keyword);

    @Operation String getLastOperation();
  }

}
