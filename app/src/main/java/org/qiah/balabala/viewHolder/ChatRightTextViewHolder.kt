package org.qiah.balabala.viewHolder

import org.qiah.balabala.MyListener.LongItemListener
import org.qiah.balabala.bean.ChatRightText
import org.qiah.balabala.databinding.ItemChatRightTextBinding

class ChatRightTextViewHolder(view: ItemChatRightTextBinding, val onLongItemListener: LongItemListener) : MultipleViewHolder<ItemChatRightTextBinding, ChatRightText>(view) {
    override fun setHolder(entity: ChatRightText) {
        view.messageTv.text = entity.text
        view.root.setOnLongClickListener {
            val location = IntArray(2)
            view.root.getLocationOnScreen(location)
            onLongItemListener.onLongClick(location, absoluteAdapterPosition, entity)
            view.root.alpha = 0.7f
            true
        }
    }

    override fun setHolder(entity: ChatRightText, payload: Any) {
        super.setHolder(entity, payload)
        view.root.alpha = 1f
    }
}
