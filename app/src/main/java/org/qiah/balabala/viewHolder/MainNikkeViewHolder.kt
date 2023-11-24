package org.qiah.balabala.viewHolder

import android.util.Log
import org.qiah.balabala.MyListener.ClickItemListener
import org.qiah.balabala.MyListener.LongItemListener
import org.qiah.balabala.bean.Chat
import org.qiah.balabala.databinding.ItemNikkeMainBinding
import org.qiah.balabala.util.load
import org.qiah.balabala.util.singleClick

class MainNikkeViewHolder(binding: ItemNikkeMainBinding, val listener: ClickItemListener<Chat>? = null, val onLong: LongItemListener) : MultipleViewHolder<ItemNikkeMainBinding, Chat>(binding) {
    companion object {
        var a = 1
    }
    override fun setHolder(entity: Chat) {
        Log.d("MainNikk", "setHolder: +" + this.toString() + entity)
        view.nikkeIv.load(entity.avatar)
        view.nikkeTv.text = entity.name
        view.messageTv.text = entity.news
        view.root.singleClick(time = 100L) {
            listener?.onClick(entity)
            listener?.onClick(entity, bindingAdapterPosition)
        }
        view.root.setOnLongClickListener {
            val a = IntArray(2)
            it.getLocationOnScreen(a)
            val b = intArrayOf(a[0], a[1], it.width, it.height)
            onLong.onLongClick(b, absoluteAdapterPosition, entity)
            true
        }
    }

    override fun setHolder(entity: Chat, payload: Any) {
        super.setHolder(entity, payload)
        Log.d("MainNikk", "setHolder: ${a++}")
    }
}
