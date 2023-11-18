package org.qiah.balabala.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.qiah.balabala.SQLite.MyDataBaseHelper
import org.qiah.balabala.bean.ChatLeftImage
import org.qiah.balabala.bean.ChatLeftText
import org.qiah.balabala.bean.ChatRightImage
import org.qiah.balabala.bean.ChatRightText
import org.qiah.balabala.bean.Message
import org.qiah.balabala.databinding.ItemChatLeftImageBinding
import org.qiah.balabala.databinding.ItemChatLeftTextBinding
import org.qiah.balabala.databinding.ItemChatRightImageBinding
import org.qiah.balabala.databinding.ItemChatRightTextBinding
import org.qiah.balabala.databinding.ItemNarrationBinding
import org.qiah.balabala.databinding.ItemSplitLineBinding
import org.qiah.balabala.util.MultipleType
import org.qiah.balabala.util.MyType
import org.qiah.balabala.viewHolder.ChatLeftImageViewHolder
import org.qiah.balabala.viewHolder.ChatLeftTextViewHolder
import org.qiah.balabala.viewHolder.ChatRightImageViewHolder
import org.qiah.balabala.viewHolder.ChatRightTextViewHolder
import org.qiah.balabala.viewHolder.NarrationViewHolder
import org.qiah.balabala.viewHolder.SplitViewHolder

class ChatAdapter(val db: MyDataBaseHelper) : MultipleTypeAdapter() {
    val getPre: (Int) -> MultipleType = { it -> data.get(it) }
    override fun createViewHolder(
        i: Int,
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup
    ): RecyclerView.ViewHolder? = when (i) {
        MyType.CHAT_LEFT_TEXT -> ChatLeftTextViewHolder(ItemChatLeftTextBinding.inflate(layoutInflater, viewGroup, false), getPre)
        MyType.CHAT_RIGHT_TEXT -> ChatRightTextViewHolder(ItemChatRightTextBinding.inflate(layoutInflater, viewGroup, false))
        MyType.CHAT_LEFT_IMAGE -> ChatLeftImageViewHolder(ItemChatLeftImageBinding.inflate(layoutInflater, viewGroup, false), getPre)
        MyType.CHAT_RIGHT_IMAGE -> ChatRightImageViewHolder(ItemChatRightImageBinding.inflate(layoutInflater, viewGroup, false))
        MyType.CHAT_SPLIT_LINE -> SplitViewHolder(ItemSplitLineBinding.inflate(layoutInflater, viewGroup, false))
        MyType.CHAT_NARRATION -> NarrationViewHolder(ItemNarrationBinding.inflate(layoutInflater, viewGroup, false))
        else -> null
    }
    fun addMessages(messages: List<Message>?) {
        if (messages == null || messages.isEmpty()) {
            return
        }
        val start = itemCount
        val cnt = messages.size
        messages.forEach {
            when (it.type) {
                1 -> {
                    if (it.nikkeId == -1) {
                        data.add(ChatRightText(it.content))
                    } else {
                        val nikke = db.selectNikkeById(it.nikkeId)
                        data.add(ChatLeftText(it.content, nikke))
                    }
                }
                2 -> {
                    data.add(object : MyType(it.content) {
                        override fun viewType(): Int = CHAT_NARRATION
                    })
                }
                3 -> {
                    data.add(object : MyType(it.content) {
                        override fun viewType(): Int = CHAT_SPLIT_LINE
                    })
                }
                4 -> {
                    if (it.nikkeId == -1) {
                        data.add(ChatRightImage(it.content))
                    } else {
                        val nikke = db.selectNikkeById(it.nikkeId)
                        data.add(ChatLeftImage(it.content, nikke))
                    }
                }
            }
        }
        this.notifyItemRangeChanged(start, cnt)
    }
}
