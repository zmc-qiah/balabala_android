package org.qiah.balabala.viewHolder

import org.qiah.balabala.MyListener.LongItemListener
import org.qiah.balabala.databinding.ItemSplitLineBinding
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.getWidth

class SplitViewHolder(view: ItemSplitLineBinding, val onLongItemListener: LongItemListener, val width: Int = getWidth()) : MultipleViewHolder<ItemSplitLineBinding, MyType>(view) {
    override fun setHolder(entity: MyType) {
        view.splitTv.maxWidth = width / 2
        view.splitTv.text = entity.data
        view.root.setOnLongClickListener {
            val location = IntArray(2)
            view.root.getLocationOnScreen(location)
            onLongItemListener.onLongClick(location, absoluteAdapterPosition, entity)
            view.root.alpha = 0.7f
            true
        }
    }
    override fun setHolder(entity: MyType, payload: Any) {
        super.setHolder(entity, payload)
        view.root.alpha = 1f
    }
}
