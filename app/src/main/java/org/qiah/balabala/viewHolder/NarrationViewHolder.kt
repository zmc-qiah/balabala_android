package org.qiah.balabala.viewHolder

import org.qiah.balabala.MyListener.LongItemListener
import org.qiah.balabala.databinding.ItemNarrationBinding
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.getWidth

class NarrationViewHolder(view: ItemNarrationBinding, val onLongItemListener: LongItemListener, val width: Int = getWidth()) : MultipleViewHolder<ItemNarrationBinding, MyType>(view) {
    override fun setHolder(entity: MyType) {
        view.narrationTv.maxWidth = (width * 0.7).toInt()
        view.narrationTv.text = entity.data
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
