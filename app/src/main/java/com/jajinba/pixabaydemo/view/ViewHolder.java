package com.jajinba.pixabaydemo.view;


import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jajinba.pixabaydemo.R;

import java.lang.ref.WeakReference;

/**
 * ViewHolder Best Practice helper class
 */
public class ViewHolder extends RecyclerView.ViewHolder {

  private SparseArray<View> mViews;

  public ViewHolder(@LayoutRes int itemLayoutId, ViewGroup parent) {
    this(itemLayoutId, parent, null, null);
  }

  public ViewHolder(View itemView, ViewGroup parent, OnRecyclerViewItemClickListener clickListener,
                    OnRecyclerViewItemLongClickListener longClickListener) {
    super(itemView);
    mViews = new SparseArray<>();
    itemView.setTag(R.id.tag_viewholder, this);

    if (clickListener != null) {
      itemView.setOnClickListener(new OnViewHolderItemClickListener(clickListener));
    }

    if (longClickListener != null) {
      itemView.setOnLongClickListener(new OnViewHolderItemLongClickListener(longClickListener));
    }
  }

  public ViewHolder(@LayoutRes int itemLayoutId, @NonNull ViewGroup parent,
                    OnRecyclerViewItemClickListener clickListener,
                    OnRecyclerViewItemLongClickListener longClickListener) {
    this(LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false), parent,
        clickListener, longClickListener);
  }

  /**
   * Get {@link View} with resource id
   *
   * @param viewId target {@link View}'s resource id
   * @param <T>    {@link View} class type
   * @return target {@link View}
   */
  public <T extends View> T getView(@IdRes int viewId) {
    View view = mViews.get(viewId);
    if (view == null) {
      view = itemView.findViewById(viewId);
      mViews.put(viewId, view);
    }
    return (T) view;
  }

  /**
   * Set text on target {@link View}
   *
   * @param viewId target {@link View}'s resource id
   * @param text   text to set
   * @return {@link ViewHolder}
   */
  public ViewHolder setText(@IdRes int viewId, CharSequence text) {
    TextView tv = getView(viewId);
    tv.setText(text);
    return this;
  }

  /**
   * Set visibility on target {@link View}
   *
   * @param viewId viewId target {@link View}'s resource id
   * @param visibility visibility to set
   * @return {@link ViewHolder}
   */
  public ViewHolder setVisibility(@IdRes int viewId, int visibility) {
    View view = getView(viewId);
    if (view != null) {
      view.setVisibility(visibility);
    }
    return this;
  }

  /**
   * On item click listener
   */
  public interface OnRecyclerViewItemClickListener extends View.OnClickListener {

    void onItemClick(ViewHolder holder, int position);

    @Override
    void onClick(View v);
  }

  private static class OnViewHolderItemClickListener implements View.OnClickListener {

    private WeakReference<OnRecyclerViewItemClickListener> mListener;

    OnViewHolderItemClickListener(OnRecyclerViewItemClickListener listener) {
      mListener = new WeakReference<>(listener);
    }

    @Override
    public void onClick(View v) {
      OnRecyclerViewItemClickListener listener = mListener.get();

      ViewHolder holder = (ViewHolder) v.getTag(R.id.tag_viewholder);
      if (holder == null || listener == null) {
        return;
      }

      final int position = holder.getAdapterPosition();
      if (position == RecyclerView.NO_POSITION) {
        return;
      }

      if (listener != null) {
        listener.onItemClick(holder, position);
      }
    }
  }

  /**
   * On item long click listener
   */
  public interface OnRecyclerViewItemLongClickListener extends View.OnLongClickListener {
    void onItemLongClick(ViewHolder holder, int position);
  }

  private static class OnViewHolderItemLongClickListener implements View.OnLongClickListener {

    private WeakReference<OnRecyclerViewItemLongClickListener> mListener;

    OnViewHolderItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
      mListener = new WeakReference<>(listener);
    }

    @Override
    public boolean onLongClick(View view) {
      OnRecyclerViewItemLongClickListener listener = mListener.get();

      ViewHolder holder = (ViewHolder) view.getTag(R.id.tag_viewholder);
      if (holder == null || listener == null) {
        return false;
      }

      final int position = holder.getAdapterPosition();
      if (position == RecyclerView.NO_POSITION) {
        return false;
      }

      if (listener != null) {
        listener.onItemLongClick(holder, position);
        return true;
      }

      return false;
    }
  }
}
