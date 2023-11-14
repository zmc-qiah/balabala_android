package org.qiah.balabala.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import org.qiah.balabala.util.MultipleType
import org.qiah.balabala.viewHolder.MultipleViewHolder

abstract class MultipleTypeAdapter<V : ViewBinding>(val view: V) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createViewHolder(
            viewType,
            LayoutInflater.from(parent.context),
            parent
        )!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder1 = holder as MultipleViewHolder<V, MultipleType>
        holder1.setHolder(data[position])
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].viewType()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    protected var data: MutableList<MultipleType> = ArrayList()
    fun add(bean: MultipleType) {
        val start = itemCount
        data.add(bean)
        notifyItemInserted(start)
    }

    fun add(list: List<MultipleType>?) {
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

    protected abstract fun createViewHolder(
        i: Int,
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup
    ): RecyclerView.ViewHolder?
}
