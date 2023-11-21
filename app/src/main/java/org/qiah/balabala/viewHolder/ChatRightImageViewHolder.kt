package org.qiah.balabala.viewHolder

import org.qiah.balabala.MyListener.LongItemListener
import org.qiah.balabala.bean.ChatRightImage
import org.qiah.balabala.databinding.ItemChatRightImageBinding
import org.qiah.balabala.util.getWidth
import org.qiah.balabala.util.load

class ChatRightImageViewHolder(view: ItemChatRightImageBinding, val onLongItemListener: LongItemListener) : MultipleViewHolder<ItemChatRightImageBinding, ChatRightImage>(view) {
    override fun setHolder(entity: ChatRightImage) {
        view.messageIv.load(entity.path, (getWidth() * 0.7).toInt(), 12)
        view.root.setOnLongClickListener {
            val location = IntArray(2)
            view.root.getLocationOnScreen(location)
            onLongItemListener.onLongClick(location, absoluteAdapterPosition, entity)
            view.root.alpha = 0.7f
            true
        }
    }
    override fun setHolder(entity: ChatRightImage, payload: Any) {
        super.setHolder(entity, payload)
        view.root.alpha = 1f
    }
}
