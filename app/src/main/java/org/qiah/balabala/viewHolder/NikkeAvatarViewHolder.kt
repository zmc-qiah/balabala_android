package org.qiah.balabala.viewHolder

import android.util.Log
import org.qiah.balabala.MyListener.SelectNikkeListener
import org.qiah.balabala.bean.Nikke
import org.qiah.balabala.databinding.ItemSelectNikkeBinding
import org.qiah.balabala.util.load
import org.qiah.balabala.util.nullOrNot
import org.qiah.balabala.util.singleClick

class NikkeAvatarViewHolder(view: ItemSelectNikkeBinding, var listener: SelectNikkeListener, val flag: Boolean = true) : MultipleViewHolder<ItemSelectNikkeBinding, Nikke>(view) {
    override fun setHolder(entity: Nikke) {
        Log.d("NikkeAvata", "setHolder: $entity")
        entity.avatarId.nullOrNot(
            {
                view.avatarIv.load(entity.avatarPath, 6)
            },
            {
                view.avatarIv.load(entity.avatarId!!, 6)
            }
        )
        view.aView.tag = false
        view.aView.singleClick(time = 100L) {
            val b = view.aView.tag as Boolean
            if (!b) {
                if (flag) view.aView.alpha = 0.4f
                listener.select(entity)
            } else {
                if (flag) view.aView.alpha = 0f
                listener.unSelect(entity)
            }
            view.aView.tag = !b
        }
    }
}
