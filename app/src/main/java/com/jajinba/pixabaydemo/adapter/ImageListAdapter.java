package com.jajinba.pixabaydemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.model.PixabayImageObject;
import com.jajinba.pixabaydemo.utils.ArrayUtils;
import com.jajinba.pixabaydemo.view.ViewHolder;
import com.jajinba.pixabaydemo.view.fragment.BaseFragment;

import java.util.List;


public class ImageListAdapter extends RecyclerView.Adapter<ViewHolder> {

  private BaseFragment mFragment;
  private List<PixabayImageObject> mImageList;

  public ImageListAdapter(BaseFragment fragment, List<PixabayImageObject> imageList) {
    mFragment = fragment;
    mImageList = imageList;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(R.layout.item_image, parent);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    if (mFragment.isFragmentValid() && ArrayUtils.getLengthSafe(mImageList) > position) {
      Glide.with(mFragment)
          .load(mImageList.get(position).getWebformatUrl())
          .placeholder(R.drawable.placeholder)
          .crossFade()
          .into((ImageView) holder.getView(R.id.image_view));
    }
  }

  @Override
  public int getItemCount() {
    return ArrayUtils.getLengthSafe(mImageList);
  }

}
