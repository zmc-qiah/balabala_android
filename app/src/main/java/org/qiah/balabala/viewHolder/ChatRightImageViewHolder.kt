package org.qiah.balabala.viewHolder

import org.qiah.balabala.bean.ChatRightImage
import org.qiah.balabala.databinding.ItemChatRightImageBinding
import org.qiah.balabala.util.load

class ChatRightImageViewHolder(view: ItemChatRightImageBinding) : MultipleViewHolder<ItemChatRightImageBinding, ChatRightImage>(view) {
    override fun setHolder(entity: ChatRightImage) {
        view.messageIv.load(entity.path, 8)
    }
}
