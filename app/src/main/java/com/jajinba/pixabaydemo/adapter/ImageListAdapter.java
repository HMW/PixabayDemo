package com.jajinba.pixabaydemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jajinba.pixabaydemo.R;
import com.jajinba.pixabaydemo.model.PixabayImageObject;
import com.jajinba.pixabaydemo.utils.ArrayUtils;
import com.jajinba.pixabaydemo.view.ViewHolder;
import com.jajinba.pixabaydemo.view.fragment.ListFragment;

import java.util.ArrayList;
import java.util.List;


public class ImageListAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final int VIEW_TYPE_ITEM = 5566;
  private final int VIEW_TYPE_LOADING = 5567;

  private ListFragment mFragment;
  private List<PixabayImageObject> mImageList;

  public ImageListAdapter(ListFragment fragment) {
    mFragment = fragment;
    mImageList = new ArrayList<>();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return viewType == VIEW_TYPE_ITEM ? new ViewHolder(R.layout.item_image, parent) :
        new ViewHolder(R.layout.item_loading, parent);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    if (getItemViewType(position) == VIEW_TYPE_ITEM) {
      if (mFragment.isFragmentValid() && ArrayUtils.getLengthSafe(mImageList) > position) {
        Glide.with(mFragment)
            .load(mImageList.get(position).getWebformatUrl())
            .placeholder(R.drawable.placeholder)
            // workaround, refer glide issue 542 on Github
            .dontAnimate()
            .override(mImageList.get(position).getWebformatWidth(),
                mImageList.get(position).getWebformatHeight())
            .into((ImageView) holder.getView(R.id.image_view));
      }
    } else if (getItemViewType(position) == VIEW_TYPE_LOADING) {
      holder.setVisibility(R.id.load_more_textview, View.VISIBLE);
      holder.setVisibility(R.id.progress_bar, View.GONE);

      // TODO should define setOnClickListener in ViewHolder
      holder.getView(R.id.load_more_textview).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          holder.setVisibility(R.id.load_more_textview, View.GONE);
          holder.setVisibility(R.id.progress_bar, View.VISIBLE);

          mFragment.loadMore();
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    // +1 for bottom loading item
    return ArrayUtils.getLengthSafe(mImageList) + 1;
  }

  @Override
  public int getItemViewType(int position) {
    return ArrayUtils.getLengthSafe(mImageList) > position ? VIEW_TYPE_ITEM : VIEW_TYPE_LOADING;
  }

  public void updateList(List<PixabayImageObject> imageList) {

    Log.e(ImageListAdapter.class.getSimpleName(), "old list: " + ArrayUtils.getLengthSafe(new ArrayList<>(mImageList)));
    Log.e(ImageListAdapter.class.getSimpleName(), "new list: " + ArrayUtils.getLengthSafe(imageList));

    mImageList.clear();
    mImageList.addAll(imageList);
  }

}

