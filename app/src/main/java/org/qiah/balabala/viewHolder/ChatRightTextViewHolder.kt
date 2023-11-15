package org.qiah.balabala.viewHolder

import org.qiah.balabala.bean.ChatRightText
import org.qiah.balabala.databinding.ItemChatRightTextBinding
class ChatRightTextViewHolder(view: ItemChatRightTextBinding) : MultipleViewHolder<ItemChatRightTextBinding, ChatRightText>(view) {
    override fun setHolder(entity: ChatRightText) {
        view.messageTv.text = entity.text
    }
}
