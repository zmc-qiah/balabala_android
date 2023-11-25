package org.qiah.balabala.viewHolder

import org.qiah.balabala.MyListener.LongItemListener
import org.qiah.balabala.databinding.ItemThingBinding
import org.qiah.balabala.util.MyType

class ThingViewHolder(view: ItemThingBinding, val onLongItemListener: LongItemListener) : MultipleViewHolder<ItemThingBinding, MyType>(view) {
    override fun setHolder(entity: MyType) {
        view.aTv.text = "前往${entity.data}的个人剧情"
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