package org.qiah.balabala.viewHolder

import org.qiah.balabala.databinding.ItemNarrationBinding
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.getWidth

class NarrationViewHolder(view: ItemNarrationBinding, val width: Int = getWidth()) : MultipleViewHolder<ItemNarrationBinding, MyType>(view) {
    override fun setHolder(entity: MyType) {
        view.narrationTv.maxWidth = (width * 0.7).toInt()
        view.narrationTv.text = entity.data
    }
}
