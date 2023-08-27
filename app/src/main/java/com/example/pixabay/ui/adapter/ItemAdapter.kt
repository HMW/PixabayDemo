package com.example.pixabay.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.pixabay.R
import com.example.pixabay.data.datasource.ImageInfo

class ItemAdapter(
  private val images: List<ImageInfo>
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

  private val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

  class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val imageView: ImageView = view.findViewById(R.id.iv_item)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    return ItemViewHolder(
      LayoutInflater
        .from(parent.context)
        .inflate(R.layout.item_image, parent, false)
    )
  }

  override fun getItemCount(): Int {
    return images.size
  }

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    val item = images[position]
    Glide.with(holder.itemView.context)
      .load(item.previewURL)
      .apply(requestOptions)
      .placeholder(android.R.drawable.progress_indeterminate_horizontal)
      .error(android.R.drawable.stat_notify_error)
      .into(holder.imageView)
  }

}
