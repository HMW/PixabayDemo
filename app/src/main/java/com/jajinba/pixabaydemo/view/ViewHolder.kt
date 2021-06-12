package com.jajinba.pixabaydemo.view

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.jajinba.pixabaydemo.R
import java.lang.ref.WeakReference

/**
 * ViewHolder Best Practice helper class
 */
class ViewHolder(
    itemView: View,
    parent: ViewGroup?,
    clickListener: OnRecyclerViewItemClickListener?,
    longClickListener: OnRecyclerViewItemLongClickListener?
) : RecyclerView.ViewHolder(itemView) {
    private val mViews: SparseArray<View?>?

    constructor(@LayoutRes itemLayoutId: Int, parent: ViewGroup) : this(
        itemLayoutId,
        parent,
        null,
        null
    )

    constructor(
        @LayoutRes itemLayoutId: Int, parent: ViewGroup,
        clickListener: OnRecyclerViewItemClickListener?,
        longClickListener: OnRecyclerViewItemLongClickListener?
    ) : this(
        LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false), parent,
        clickListener, longClickListener
    )

    /**
     * Get [View] with resource id
     *
     * @param viewId target [View]'s resource id
     * @param <T>    [View] class type
     * @return target [View]
    </T> */
    fun <T : View?> getView(@IdRes viewId: Int): T? {
        var view = mViews?.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews?.put(viewId, view)
        }
        return view as T?
    }

    /**
     * Set text on target [View]
     *
     * @param viewId target [View]'s resource id
     * @param text   text to set
     * @return [ViewHolder]
     */
    fun setText(@IdRes viewId: Int, text: CharSequence?): ViewHolder? {
        val tv = getView<TextView?>(viewId)
        tv?.text = text
        return this
    }

    /**
     * Set visibility on target [View]
     *
     * @param viewId     viewId target [View]'s resource id
     * @param visibility visibility to set
     * @return [ViewHolder]
     */
    fun setVisibility(@IdRes viewId: Int, visibility: Int): ViewHolder? {
        val view = getView<View?>(viewId)
        if (view != null) {
            view.visibility = visibility
        }
        return this
    }

    /**
     * On item click listener
     */
    interface OnRecyclerViewItemClickListener : View.OnClickListener {
        fun onItemClick(holder: ViewHolder?, position: Int)
        override fun onClick(v: View?)
    }

    private class OnViewHolderItemClickListener(listener: OnRecyclerViewItemClickListener?) :
        View.OnClickListener {
        private val mListener: WeakReference<OnRecyclerViewItemClickListener?>?
        override fun onClick(v: View?) {
            val listener = mListener?.get()
            val holder = v?.getTag(R.id.tag_viewholder) as ViewHolder
            if (holder == null || listener == null) {
                return
            }
            val position = holder.adapterPosition
            if (position == RecyclerView.NO_POSITION) {
                return
            }
            if (listener != null) {
                listener.onItemClick(holder, position)
            }
        }

        init {
            mListener = WeakReference(listener)
        }
    }

    /**
     * On item long click listener
     */
    interface OnRecyclerViewItemLongClickListener : OnLongClickListener {
        fun onItemLongClick(holder: ViewHolder?, position: Int)
    }

    private class OnViewHolderItemLongClickListener(listener: OnRecyclerViewItemLongClickListener?) :
        OnLongClickListener {
        private val mListener: WeakReference<OnRecyclerViewItemLongClickListener?>?
        override fun onLongClick(view: View?): Boolean {
            val listener = mListener?.get()
            val holder = view?.getTag(R.id.tag_viewholder) as ViewHolder
            if (holder == null || listener == null) {
                return false
            }
            val position = holder.adapterPosition
            if (position == RecyclerView.NO_POSITION) {
                return false
            }
            if (listener != null) {
                listener.onItemLongClick(holder, position)
                return true
            }
            return false
        }

        init {
            mListener = WeakReference(listener)
        }
    }

    init {
        mViews = SparseArray()
        itemView.setTag(R.id.tag_viewholder, this)
        if (clickListener != null) {
            itemView.setOnClickListener(OnViewHolderItemClickListener(clickListener))
        }
        if (longClickListener != null) {
            itemView.setOnLongClickListener(OnViewHolderItemLongClickListener(longClickListener))
        }
    }
}
