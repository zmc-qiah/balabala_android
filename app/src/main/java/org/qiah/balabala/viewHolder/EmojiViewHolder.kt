package org.qiah.balabala.viewHolder

import org.qiah.balabala.MyListener.ClickItemListener
import org.qiah.balabala.MyListener.LongItemListener
import org.qiah.balabala.databinding.ItemEmojiBinding
import org.qiah.balabala.util.MyType
import org.qiah.balabala.util.getWidth
import org.qiah.balabala.util.load
import org.qiah.balabala.util.singleClick

class EmojiViewHolder(view: ItemEmojiBinding, val listener: ClickItemListener<String>, val onLong: LongItemListener) : MultipleViewHolder<ItemEmojiBinding, MyType>(view) {
    override fun setHolder(entity: MyType) {
        val width = getWidth() / 5
        view.root.layoutParams.width = width
        view.root.layoutParams.height = width
        view.emojiIv.load(entity.data, 6)
        view.emojiIv.singleClick {
            listener.onClick(entity.data)
            val s = entity.data
        }
        view.emojiIv.setOnLongClickListener {
            val a = IntArray(2)
            view.root.getLocationOnScreen(a)
            val b = intArrayOf(a[0], a[1], view.root.width, view.root.height)
            onLong.onLongClick(b, absoluteAdapterPosition, entity)
            true
        }
    }
}
