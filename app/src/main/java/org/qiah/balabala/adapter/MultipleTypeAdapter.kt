package org.qiah.balabala.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.qiah.balabala.util.MultipleType
import org.qiah.balabala.viewHolder.MultipleViewHolder

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class MultipleTypeAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createViewHolder(
            viewType,
            LayoutInflater.from(parent.context),
            parent
        )!!
    }
    protected abstract fun createViewHolder(
        i: Int,
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup
    ): RecyclerView.ViewHolder?
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("MainNikk", "onBindViewHolder: " + position)
        val holder1 = holder as MultipleViewHolder<*, MultipleType>
        holder1.setHolder(data[position])
    }
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (!payloads.isNullOrEmpty()) {
            val holder1 = holder as MultipleViewHolder<*, MultipleType>
            holder.setHolder(data.get(position), payloads[0])
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].viewType()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    protected var data: MutableList<MultipleType> = ArrayList()
    fun add(bean: MultipleType?) {
        bean?.let {
            val start = itemCount
            data.add(bean)
            notifyItemInserted(start)
        }
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
    fun clearAndAdd(list: List<MultipleType>?) {
        this.clear()
        this.add(list)
    }
    fun insert(start: Int, beans: List<MultipleType>?) {
        beans?.let {
            val cnt = itemCount
            val l = beans.size
            data.addAll(start, beans)
            notifyItemRangeRemoved(start, cnt - start)
            notifyItemRangeInserted(start, cnt - start + l)
        }
    }
    fun insert(start: Int, bean: MultipleType?) {
        bean?.let {
            val cnt = itemCount
            val l = 1
            data.add(start, bean)
            notifyItemRangeRemoved(start, cnt - start)
            notifyItemRangeInserted(start, cnt - start + l)
        }
    }
    fun remove(bean: MultipleType?) {
        bean?.let {
            if (data.contains(bean)) {
                val i = data.indexOf(bean)
                data.removeAt(i)
                notifyItemRemoved(i)
            }
        }
    }
    fun moveToPostion(bean: MultipleType?, position: Int) {
        bean?.let {
            if (data.contains(bean)) {
                val i = data.indexOf(bean)
                if (position != i) {
                    data.removeAt(i)
                    data.add(position, bean)
                    notifyItemRemoved(i)
                    notifyItemInserted(position)
                }
            }
        }
    }
}
