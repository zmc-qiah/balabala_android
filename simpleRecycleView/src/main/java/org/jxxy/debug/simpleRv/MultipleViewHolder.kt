package org.jxxy.debug.simpleRv

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
abstract class MultipleViewHolder<V : ViewBinding?, T : MultipleType?>(var view: V) :
    RecyclerView.ViewHolder(view!!.root) {
    var context: Context
        protected set

    init {
        context = view!!.root.context
    }

    abstract fun setHolder(entity: T)

    open fun setHolder(
        entity: T,
        payload: Any,
    ) {
    }
}
