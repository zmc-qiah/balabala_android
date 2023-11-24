package org.qiah.balabala.MyListener

import org.qiah.balabala.bean.Chat

interface ClickLongChatDialogListener {
    fun onMoveTo(entry: Chat, position: Int)
    fun onUpdate(entry: Chat, position: Int)
    fun onDelete(entry: Chat, position: Int)
}