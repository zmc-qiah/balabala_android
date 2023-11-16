package org.qiah.balabala.viewHolder

import org.qiah.balabala.bean.ChatLeftImage
import org.qiah.balabala.databinding.ItemChatLeftImageBinding
import org.qiah.balabala.util.getWidth
import org.qiah.balabala.util.gone
import org.qiah.balabala.util.load
import org.qiah.balabala.util.nullOrNot
import org.qiah.balabala.util.show

class ChatLeftImageViewHolder(view: ItemChatLeftImageBinding) : MultipleViewHolder<ItemChatLeftImageBinding, ChatLeftImage>(view) {
    override fun setHolder(entity: ChatLeftImage) {
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
        view.run { messageIv.load(entity.path, (getWidth() * 0.7).toInt(), 16) }
    }
}
