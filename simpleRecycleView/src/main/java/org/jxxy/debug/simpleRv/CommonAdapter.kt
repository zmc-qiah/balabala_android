package org.jxxy.debug.simpleRv

import android.view.LayoutInflater
import android.view.ViewGroup

class CommonAdapter : MultipleTypeAdapter() {
    override fun createViewHolder(
        i: Int,
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup,
    ): MultipleViewHolder<*, MultipleType>? = null
}
