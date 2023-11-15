package org.qiah.balabala.viewHolder

import org.qiah.balabala.MyListener.SelectNikkeListener
import org.qiah.balabala.bean.Nikke
import org.qiah.balabala.databinding.ItemSelectNikkeBinding
import org.qiah.balabala.util.load
import org.qiah.balabala.util.nullOrNot
import org.qiah.balabala.util.singleClick

class NikkeAvatarViewHolder(view: ItemSelectNikkeBinding, var listener: SelectNikkeListener) : MultipleViewHolder<ItemSelectNikkeBinding, Nikke>(view) {
    override fun setHolder(entity: Nikke) {
        entity.avatarId.nullOrNot(
            {
                view.avatarIv.load(entity.avatarPath, 6)
            },
            {
                view.avatarIv.load(entity.avatarId!!, 6)
            }
        )
        view.root.tag = false
        view.root.singleClick(time = 100L) {
            val b = view.root.tag as Boolean
            if (!b) {
                view.aView.alpha = 0.4f
                listener.select(entity)
            } else {
                view.aView.alpha = 0f
                listener.unSelect(entity)
            }
            view.root.tag = !b
        }
    }
}
