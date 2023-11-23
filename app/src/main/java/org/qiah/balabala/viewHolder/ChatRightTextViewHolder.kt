package org.qiah.balabala.viewHolder

import android.util.Log
import android.view.ViewTreeObserver
import org.qiah.balabala.MyListener.LongItemListener
import org.qiah.balabala.bean.ChatRightText
import org.qiah.balabala.databinding.ItemChatRightTextBinding
import org.qiah.balabala.util.dp

class ChatRightTextViewHolder(view: ItemChatRightTextBinding, val onLongItemListener: LongItemListener) : MultipleViewHolder<ItemChatRightTextBinding, ChatRightText>(view) {
    override fun setHolder(entity: ChatRightText) {
        Log.d("asacsacsa", "setHolder: " + entity.text)
        view.messageTv.text = entity.text
        view.root.setOnLongClickListener {
            val location = IntArray(2)
            view.root.getLocationOnScreen(location)
            onLongItemListener.onLongClick(location, absoluteAdapterPosition, entity)
            view.root.alpha = 0.7f
            true
        }
        view.messageTv.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val x1 = view.pointView.x
                val x2 = view.rgIv.x
                val x3: Float = 5f.dp()
                Log.d("RightTextView", "setHolder: " + x1 + "" + x2 + "" + x3 + "" + (x2 - x1 + x3).toInt())
                view.messageTv.maxWidth = (x2 - x1 + x3).toInt()
                view.messageTv.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
    }

    override fun setHolder(entity: ChatRightText, payload: Any) {
        super.setHolder(entity, payload)
        view.root.alpha = 1f
    }
}
