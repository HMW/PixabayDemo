package com.jajinba.pixabaydemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.model.PixabayImageObject;
import com.jajinba.pixabaydemo.utils.ArrayUtils;
import com.jajinba.pixabaydemo.view.ViewHolder;

import java.util.List;


public class ImageListAdapter extends RecyclerView.Adapter<ViewHolder> {

  private List<PixabayImageObject> mImageList;

  public ImageListAdapter(List<PixabayImageObject> imageList) {
    mImageList = imageList;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(R.layout.item_image, parent);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    if (ArrayUtils.getLengthSafe(mImageList) > position) {
      holder.setText(R.id.title_tv, mImageList.get(position).getPreviewUrl());
    }
  }

  @Override
  public int getItemCount() {
    return ArrayUtils.getLengthSafe(mImageList);
  }

}
