package org.qiah.balabala.viewHolder

import org.qiah.balabala.bean.ChatLeftText
import org.qiah.balabala.databinding.ItemChatLeftTextBinding
import org.qiah.balabala.util.gone
import org.qiah.balabala.util.load
import org.qiah.balabala.util.nullOrNot
import org.qiah.balabala.util.show

class ChatLeftTextViewHolder(view: ItemChatLeftTextBinding) : MultipleViewHolder<ItemChatLeftTextBinding, ChatLeftText>(view) {
    override fun setHolder(entity: ChatLeftText) {
        entity.nikke.nullOrNot(
            {
                view.avatarIv.gone()
                view.nameTv.gone()
            },
            {
                view.avatarIv.load(it, true)
                view.nameTv.text = it.name
                view.avatarIv.show()
                view.nameTv.show()
            }
        )
        view.messageTv.text = entity.text
    }
}
