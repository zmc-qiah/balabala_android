package org.qiah.balabala.viewHolder

import org.qiah.balabala.databinding.ItemSplitLineBinding
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.getWidth

class SplitViewHolder(view: ItemSplitLineBinding, val width: Int = getWidth()) : MultipleViewHolder<ItemSplitLineBinding, MyType>(view) {
    override fun setHolder(entity: MyType) {
        view.splitTv.maxWidth = width / 2
        view.splitTv.text = entity.data
    }
}
