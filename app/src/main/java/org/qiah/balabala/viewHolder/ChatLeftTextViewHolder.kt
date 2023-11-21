package org.qiah.balabala.viewHolder

import android.view.ViewTreeObserver
import org.qiah.balabala.MyListener.LongItemListener
import org.qiah.balabala.bean.ChatLeftImage
import org.qiah.balabala.bean.ChatLeftText
import org.qiah.balabala.databinding.ItemChatLeftTextBinding
import org.qiah.balabala.util.MultipleType
import org.qiah.balabala.util.gone
import org.qiah.balabala.util.load
import org.qiah.balabala.util.show

class ChatLeftTextViewHolder(view: ItemChatLeftTextBinding, val getPre: (Int) -> MultipleType, val onLongItemListener: LongItemListener) : MultipleViewHolder<ItemChatLeftTextBinding, ChatLeftText>(view) {
    override fun setHolder(entity: ChatLeftText) {
        if (absoluteAdapterPosition == 0 ||
            entity.nikke == null
        ) {
            view.avatarIv.load(entity.nikke!!, true)
            view.nameTv.text = entity.nikke!!.name
            view.avatarIv.show()
            view.nameTv.show()
        } else {
            var t: MultipleType = getPre(absoluteAdapterPosition - 1)
            var i1 = -1
            var i2 = -2
            if (t is ChatLeftText) {
                i1 = (t as ChatLeftText).nikke?.id ?: -1
                i2 = entity.nikke?.id ?: -2
            } else if (t is ChatLeftImage) {
                i1 = (t as ChatLeftImage).nikke?.id ?: -1
                i2 = entity.nikke?.id ?: -2
            }
            if (i1.equals(i2)) {
                view.avatarIv.gone()
                view.nameTv.gone()
            } else {
                view.avatarIv.load(entity.nikke!!, true)
                view.nameTv.text = entity.nikke!!.name
                view.avatarIv.show()
                view.nameTv.show()
            }
        }
        view.messageTv.text = entity.text
        view.root.setOnLongClickListener {
            view.root.alpha = 0.7f
            val location = IntArray(2)
            view.root.getLocationOnScreen(location)
            onLongItemListener.onLongClick(location, absoluteAdapterPosition, entity)
            true
        }
        view.messageTv.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val x1 = view.messageTv.x
                val x2 = view.pointView.x
                view.messageTv.maxWidth = (x2 - x1).toInt()
                return true
            }
        })
    }
    override fun setHolder(entity: ChatLeftText, payload: Any) {
        super.setHolder(entity, payload)
        view.root.alpha = 1f
    }
}
