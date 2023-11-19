package org.qiah.balabala.MyListener

import org.qiah.balabala.bean.ChatItem

interface ClickModifyDialogListener {
    fun onInsert(entry: ChatItem, position: Int)
    fun onUpdate(entry: ChatItem, position: Int)
    fun onDelete(entry: ChatItem, position: Int)
}
