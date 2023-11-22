package org.qiah.balabala.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import org.qiah.balabala.MyListener.ClickModifyDialogListener
import org.qiah.balabala.MyListener.LongItemListener
import org.qiah.balabala.SQLite.MyDataBaseHelper
import org.qiah.balabala.bean.ChatItem
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
import org.qiah.balabala.dialog.ModifyDialog
import org.qiah.balabala.util.MultipleType
import org.qiah.balabala.util.MyType
import org.qiah.balabala.viewHolder.ChatLeftImageViewHolder
import org.qiah.balabala.viewHolder.ChatLeftTextViewHolder
import org.qiah.balabala.viewHolder.ChatRightImageViewHolder
import org.qiah.balabala.viewHolder.ChatRightTextViewHolder
import org.qiah.balabala.viewHolder.NarrationViewHolder
import org.qiah.balabala.viewHolder.SplitViewHolder

class ChatAdapter(val db: MyDataBaseHelper, val fm: FragmentManager, val listener: ClickModifyDialogListener) : MultipleTypeAdapter() {
    val getPre: (Int) -> MultipleType = { it -> data.get(it) }
    var oldPosition: Int = -1
    var newPosition: Int = -1
    val onLongItemListener: LongItemListener by lazy {
        object : LongItemListener {
            override fun onLongClick(location: IntArray, position: Int, entry: MyType) {
                oldPosition = newPosition
                newPosition = position
                ModifyDialog(
                    location,
                    listener,
                    entry as ChatItem,
                    position
                ) { if (newPosition != -1) notifyItemChanged(newPosition, true) }.show(fm)
            }
        }
    }

    override fun createViewHolder(
        i: Int,
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup
    ): RecyclerView.ViewHolder? = when (i) {
        MyType.CHAT_LEFT_TEXT -> ChatLeftTextViewHolder(
            ItemChatLeftTextBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            ),
            getPre,
            onLongItemListener
        )

        MyType.CHAT_RIGHT_TEXT -> ChatRightTextViewHolder(
            ItemChatRightTextBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            ),
            onLongItemListener
        )

        MyType.CHAT_LEFT_IMAGE -> ChatLeftImageViewHolder(
            ItemChatLeftImageBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            ),
            getPre,
            onLongItemListener
        )

        MyType.CHAT_RIGHT_IMAGE -> ChatRightImageViewHolder(
            ItemChatRightImageBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            ),
            onLongItemListener
        )

        MyType.CHAT_SPLIT_LINE -> SplitViewHolder(
            ItemSplitLineBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            ),
            onLongItemListener
        )

        MyType.CHAT_NARRATION -> NarrationViewHolder(
            ItemNarrationBinding.inflate(
                layoutInflater,
                viewGroup,
                false
            ),
            onLongItemListener
        )

        else -> null
    }

    fun addMessages(messages: List<Message>?) {
        if (messages == null || messages.isEmpty()) {
            return
        }
        val messages = messages.sortedBy { it.postion }
        val start = itemCount
        val cnt = messages.size
        messages.forEach {
            Log.d("TAG", "addMessages: " + it.postion + it.content)
            when (it.type) {
                1 -> {
                    if (it.nikkeId == 0) {
                        data.add(ChatRightText(it, it.content))
                    } else {
                        val nikke = db.selectNikkeById(it.nikkeId)
                        data.add(ChatLeftText(it, it.content, nikke))
                    }
                }

                2 -> {
                    data.add(object : ChatItem(it, it.content) {
                        override fun viewType(): Int = CHAT_NARRATION
                    })
                }

                3 -> {
                    data.add(object : ChatItem(it, it.content) {
                        override fun viewType(): Int = CHAT_SPLIT_LINE
                    })
                }

                4 -> {
                    if (it.nikkeId == 0) {
                        data.add(ChatRightImage(it, it.content))
                    } else {
                        val nikke = db.selectNikkeById(it.nikkeId)
                        data.add(ChatLeftImage(it, it.content, nikke))
                    }
                }
            }
        }
        this.notifyItemRangeChanged(start, cnt)
    }
    fun updateMessage(entry: ChatItem?) {
        entry?.let {
            data.set(entry.message.postion, entry)
            notifyItemChanged(entry.message.postion)
        }
    }

    fun deleteMessage(message: Message?) {
        message?.let {
            data.removeAt(message.postion)
            for (i in message.postion..itemCount) {
                (data.getOrNull(i) as? ChatItem)?.let {
                    it.message.postion = it.message.postion - 1
                }
            }
            notifyItemRemoved(message.postion)
        }
    }

    fun insertMessage(entry: ChatItem) {
        this.insert(entry.message.postion, entry)
        for (i in entry.message.postion + 1..itemCount) {
            (data.getOrNull(i) as? ChatItem)?.let {
                it.message.postion = it.message.postion + 1
            }
        }
    }
}
