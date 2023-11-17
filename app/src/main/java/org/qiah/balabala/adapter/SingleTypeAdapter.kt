package org.qiah.balabala.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.qiah.balabala.viewHolder.SingleViewHolder

abstract class SingleTypeAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createViewHolder(
            LayoutInflater.from(parent.context),
            parent
        )!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder1 = holder as SingleViewHolder<*, T>
        holder1.setHolder(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    protected var data: MutableList<T> = ArrayList()
    fun add(bean: T?) {
        bean?.let {
            val start = itemCount
            data.add(bean)
            notifyItemInserted(start)
        }
    }

    fun add(list: List<T>?) {
        if (list == null || list.isEmpty()) {
            return
        }
        val start = itemCount
        val count = list.size
        data.addAll(list)
        notifyItemRangeInserted(start, count)
    }

    fun clear() {
        val count = itemCount
        data.clear()
        notifyItemRangeRemoved(0, count)
    }
    fun clearAndAdd(list: List<T>?) {
        this.clear()
        this.add(list)
    }

    protected abstract fun createViewHolder(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup
    ): RecyclerView.ViewHolder?
}
