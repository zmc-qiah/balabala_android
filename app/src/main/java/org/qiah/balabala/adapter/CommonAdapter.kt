package org.qiah.balabala.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import org.jxxy.debug.simpleRv.MultipleType
import org.jxxy.debug.simpleRv.MultipleTypeAdapter
import org.jxxy.debug.simpleRv.MultipleViewHolder

class CommonAdapter : MultipleTypeAdapter() {
    override fun createViewHolder(
        i: Int,
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup,
    ): MultipleViewHolder<*, MultipleType>? = null
}
